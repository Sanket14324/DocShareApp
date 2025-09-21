
var messageContainer = document.getElementById("messageContainer");
messageContainer.scrollTop = messageContainer.scrollHeight;

const fileInput = document.getElementById('fileInput');
fileInput.addEventListener('change', function () {
    const selectedFiles = fileInput.files;
    if (selectedFiles.length > 0) {
        document.getElementById('selectedFileName').textContent = `${Array.from(selectedFiles).map(file => file.name).join(', ')}`;
    } else {
        // If no files were selected, clear the displayed text
        document.getElementById('selectedFileName').textContent = '';
    }
});
function reloadPage() {
    location.reload();
}
setTimeout(reloadPage, 30000);