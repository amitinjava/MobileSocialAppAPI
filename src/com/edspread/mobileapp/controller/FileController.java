/**
 * 
 */
package com.edspread.mobileapp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.edspread.mobileapp.constants.MobileAppConstant;
import com.edspread.mobileapp.dto.ResponseData;
import com.edspread.mobileapp.utils.DateUtil;
import com.edspread.mobileapp.utils.SessionUtil;

/**
 * @author Amit.Kumar1
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {
	
	
	/**
	 * @param inputFile
	 * @param thumbnailFile
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, headers=("content-type=multipart/*"))
//	public @ResponseBody ResponseData upload(@RequestParam("file") MultipartFile[] inputFiles) {
	public @ResponseBody ResponseData upload(@RequestParam("inputFile") MultipartFile inputFile,@RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
		ResponseData rd= new ResponseData();
		String contextPath = com.edspread.mobileapp.utils.SessionUtil.getContextPath();
		
		String uploadDir = contextPath + File.separator + "upload" + File.separator + DateUtil.getFormattedDateTime();
		File uploadDirFp = new File(uploadDir);
		if (!uploadDirFp.exists()) {
            if (!uploadDirFp.mkdirs()) {
               System.out.println("Failed to create directory");
               String[] errors = {"Failed to create directory"};
               rd.errors = errors;
                return rd;
            }
        }
		
		
		Map<String,String> files = new HashMap<String,String>();
		if(inputFile != null){
		String inputFileName =  inputFile.getOriginalFilename();
		String uploadedFileServerPath = "upload" + "/" + DateUtil.getFormattedDateTime() + "/"+ inputFileName;
		String actualFilePath = SessionUtil.getContextPath() + File.separator + "upload" + File.separator + DateUtil.getFormattedDateTime()+ File.separator + inputFileName;
		File destFile = new File(actualFilePath);
		try {
			FileUtils.copyFile(multipartToFile(inputFile), destFile);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uploadedFileServerPath = MobileAppConstant.SERVERHTTPPATH + uploadedFileServerPath;
		files.put("inputFile",uploadedFileServerPath);
		}
		if(thumbnailFile != null){
		String thumbnailFileName =  thumbnailFile.getOriginalFilename();
		String thumbnailFilePath = "upload" + "/" + DateUtil.getFormattedDateTime() + "/"+ thumbnailFileName;
		String thumbnailPath = SessionUtil.getContextPath() + File.separator + "upload" + File.separator + DateUtil.getFormattedDateTime()+ File.separator + thumbnailFileName;
		File destThFile = new File(thumbnailPath);
		try {
			FileUtils.copyFile(multipartToFile(thumbnailFile), destThFile);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		thumbnailFilePath = MobileAppConstant.SERVERHTTPPATH + thumbnailFilePath;
		files.put("thumbnailFile",thumbnailFilePath);
		}
		rd.data = files;
		return rd;

	}
	
	public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException 
	{
	    File convFile = new File( multipart.getOriginalFilename());
	    multipart.transferTo(convFile);
	    return convFile;
	}

	public void fileUpload(MultipartFile[] inputFiles){
		for(MultipartFile inputFile: inputFiles){
			System.out.println(inputFile.getName());
			System.out.println(inputFile.getOriginalFilename());
			
			try {
				byte[] bytes = inputFile.getBytes();
				File serverFile = new File("D:/Software/test1.jpg");
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			}
	}
	
}
