package ch.heig.amt.overflow.ui.web.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet(name = "UploadServlet", value = "/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private String uploadPath;
    private static final String IMAGE_FOLDER = "badges";

    @Override
    public void init() throws ServletException {
        super.init();
        uploadPath = getServletContext().getRealPath(IMAGE_FOLDER);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("icon");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();
        File f = new File(uploadPath + File.separator + fileName);
        java.nio.file.Files.copy(
                fileContent,
                f.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        response.getWriter().print(request.getContextPath() + "/" + IMAGE_FOLDER + "/" + f.getName());
    }

}
