package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.sanselan.ImageReadException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import utils.IdentityUtil;
import utils.JpegReader;
public class FileUpload implements Serializable{
	public static final String IMAGEPATH = "resources/uploadImg/";
	public static final String JPG = ".jpg";
	public static final String JPG_FORMAT = "jpg";
	private static final long serialVersionUID = -6157222822620092867L;
	public static String handleFileUpload(FileUploadEvent event) {
		try {
		String imageId = IdentityUtil.randomUUID();
		UploadedFile uploadedFile = event.getFile();
		InputStream inputStream;
		inputStream = uploadedFile.getInputstream();

		BufferedImage src = new JpegReader().readImage(inputstreamToFile(inputStream));

		ImageIO.write(src, JPG_FORMAT, new File(getImgPath() + imageId + JPG)); 

        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return imageId+JPG;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	
	private static File inputstreamToFile(InputStream ins) {
		File file = new File(getImgPath()+IdentityUtil.randomUUID() + "_temp");
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return file;
	}
	private static String getImgPath(){
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String prePath = context.getRealPath("/");
		return prePath + IMAGEPATH;
	}
}
