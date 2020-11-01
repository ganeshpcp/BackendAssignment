package com.statemnt.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;  
import org.w3c.dom.Element;  
import java.io.File; 
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.statemnt.model.StatementModel;
import com.statemnt.util.ExachngeDataUtil;


@RestController
public class StatementController {
	
	
	@GetMapping("/test")
	public String getTest()
	{
		return "TEST";
	}
		@PostMapping("/importStament")
	public ResponseEntity getFailedRecordscsv(@RequestParam("file") MultipartFile fileCsv) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException
	{
		List<StatementModel> readdData;
		System.out.println("import csv : "+ExachngeDataUtil.hasCSVFormat(fileCsv));
		if(ExachngeDataUtil.hasCSVFormat(fileCsv).equalsIgnoreCase("csv"))
		{
		 readdData = ExachngeDataUtil.csvFormatReq(fileCsv.getInputStream());
		List<StatementModel> dataOfFailed= ExachngeDataUtil.failedRecord(readdData);
		String status=ExachngeDataUtil.writeCsv(dataOfFailed);
		System.out.println("Status CSV : "+status);
		}
		else if(ExachngeDataUtil.hasCSVFormat(fileCsv).equalsIgnoreCase("xml"))
		{
		readdData = ExachngeDataUtil.xmlFormatReq(fileCsv);
		List<StatementModel> dataOfFailed=ExachngeDataUtil.failedRecord(readdData);
		String status=ExachngeDataUtil.writeXml(dataOfFailed);
		System.out.println("Status  XML : "+status);
		}
		else
		{
			return new ResponseEntity("Not Accepted Upload File !! ",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity("Failed Records Downloaded.. ",HttpStatus.ACCEPTED);
	}



}
