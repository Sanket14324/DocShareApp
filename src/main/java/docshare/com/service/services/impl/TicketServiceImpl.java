package docshare.com.service.services.impl;

import docshare.com.service.constant.Constant;
import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetailFile;
import docshare.com.service.model.User;
import docshare.com.service.repository.TicketRepository;
import docshare.com.service.services.ITicketDetailFileService;
import docshare.com.service.services.ITicketService;
import docshare.com.service.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements ITicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ITicketDetailFileService fileService;


    @Autowired
    private AmazonClient  s3Client;

    @Autowired
    private IUserService userService;
    @Override
    public List<Ticket> getAllTickets(String startDate, String endDate, String status) {
        if(status != null && status.equals(Constant.ALL)){
            return ticketRepository.findAll();
        }
        if(startDate != null && endDate != null && status != null){
            return filterTickets(startDate, endDate, status);
        }
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        User user = userService.getUserById(userId).get();
        if(user != null){
            return ticketRepository.findByUser(user);
        }
        return new ArrayList<Ticket>();
    }

    @Override
    public List<Ticket> getTicketByUserName(String username) {
        User user = userService.getUserByEmail(username).get();
        if(user != null){
            return ticketRepository.findByUser(user);
        }
        return new ArrayList<Ticket>();
    }

    @Override
    public String createTicket(String username, String title) {
        Optional<User> user = userService.getUserByEmail(username);
        if(user.isPresent()){
            Ticket ticket = new Ticket();
            ticket.setTitle(title);
            ticket.setStatus(Constant.OPEN);
            ticket.setCreatedDate(new Timestamp(new Date().getTime()));
            ticket.setUser(user.get());
            ticketRepository.save(ticket);
            return "success";
        }
        return "fail";
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).get();
    }

    @Override
    public String uploadFileByTicketId(Long id, MultipartFile file) {
        Ticket ticket = getTicketById(id);

        String fileLocation = s3Client.uploadFile(file);

        TicketDetailFile ticketDetailFile = new TicketDetailFile();
        ticketDetailFile.setTicket(ticket);
        ticketDetailFile.setLocation(fileLocation);
        ticketDetailFile.setFilename(file.getOriginalFilename());
        ticketDetailFile.setCreatedDate(new Timestamp(new Date().getTime()));
        return fileService.createTicketFile(ticketDetailFile);
    }

    @Override
    public List<TicketDetailFile> getTicketFilesByTicketId(Long id) {
        Ticket ticket = getTicketById(id);
        List<TicketDetailFile> list = fileService.getTicketFilesByTicketId(ticket);
        return list;
    }

    @Override
    public String deleteTheFile(Long fileId) {

        if(fileService.deleteTicketFile(fileId).equals(Constant.SUCCESS)){
            TicketDetailFile ticketDetailFile = fileService.getTicketFileById(fileId);
            String result = s3Client.deleteFile(ticketDetailFile.getLocation());
            if(result.equals(Constant.SUCCESS)){
                fileService.deleteTicketFile(fileId);
            }
            return Constant.SUCCESS;
        }
        return Constant.FAIL;
    }

    @Override
    public String changeStatus(String status, Long ticketId) {
       Ticket ticket =  getTicketById(ticketId);
       ticket.setStatus(status);
       ticketRepository.save(ticket);
       return Constant.SUCCESS;
    }

    @Override
    public List<Ticket> filterTickets(String startDate, String endDate, String status) {

        if(!startDate.equals("") && !endDate.equals("") && !status.equals(""))
       {
           try {
               DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
               List<Ticket> list = ticketRepository.findByCreatedDateBetweenAndStatus(new Timestamp(format.parse(startDate).getTime()), new Timestamp(format.parse(endDate).getTime()), status);
               return list;
           }
           catch (Exception e){
               e.printStackTrace();
           }
        }

        List<Ticket> list = ticketRepository.findByStatus(status);
        return list;

    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.delete(getTicketById(id));
    }
}