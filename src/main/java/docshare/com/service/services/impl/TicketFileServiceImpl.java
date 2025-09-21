package docshare.com.service.services.impl;

import docshare.com.service.constant.Constant;
import docshare.com.service.model.Ticket;
import docshare.com.service.model.TicketDetailFile;
import docshare.com.service.repository.TicketDetailFileRepository;
import docshare.com.service.services.ITicketDetailFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketFileServiceImpl implements ITicketDetailFileService {

    @Autowired
    private TicketDetailFileRepository fileRepository;
    @Override
    public String createTicketFile(TicketDetailFile ticketDetailFile) {

        TicketDetailFile savedFile = fileRepository.save(ticketDetailFile);

        return Constant.SUCCESS;
    }

    @Override
    public List<TicketDetailFile> getTicketFilesByTicketId(Ticket ticket) {
        return fileRepository.findByTicket(ticket);
    }

    @Override
    public String deleteTicketFile(Long id) {
        TicketDetailFile ticketDetailFile = getTicketFileById(id);
        fileRepository.delete(ticketDetailFile);
        return Constant.SUCCESS;
    }

    @Override
    public TicketDetailFile getTicketFileById(Long id) {
        TicketDetailFile ticketFile = fileRepository.findById(id).get();
        return ticketFile;
    }
}
