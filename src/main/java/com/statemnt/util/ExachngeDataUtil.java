package com.statemnt.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.statemnt.model.StatementModel;

public class ExachngeDataUtil {
	private static DecimalFormat df = new DecimalFormat("0.00");
	public static String TYPE = "csv";
	public static String TYPE1 = "xml";

	public static String hasCSVFormat(MultipartFile file) {
		System.out.println("File type "+file.getOriginalFilename().split("\\.")[1]);
		if (TYPE.equalsIgnoreCase(file.getOriginalFilename().split("\\.")[1]) || TYPE1.equalsIgnoreCase(file.getOriginalFilename().split("\\.")[1])) {
			return file.getOriginalFilename().split("\\.")[1];

		}
		return "false";


	}

	public static List<StatementModel> csvFormatReq(InputStream is)
			throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<StatementModel> smodelsList = new ArrayList<StatementModel>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				StatementModel sModel = new StatementModel(Long.parseLong(csvRecord.get("Reference")),
						csvRecord.get("AccountNumber"), csvRecord.get("Description"),
						Float.parseFloat(csvRecord.get("Start Balance")), Float.parseFloat(csvRecord.get("Mutation")),
						Float.parseFloat(csvRecord.get("End Balance")));

				smodelsList.add(sModel);
			}

					
			return smodelsList;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

	
	
	
	
	
	public static List<StatementModel> xmlFormatReq(MultipartFile is)
			 {List<StatementModel> xmlStatemts= new ArrayList<StatementModel>();
				try {
				    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				    Document doc = dBuilder.parse(is.getInputStream());
				    doc.getDocumentElement().normalize();

				    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				    NodeList nList = doc.getElementsByTagName("record");
				    System.out.println("----------------------------");

				    for (int temp = 0; temp < nList.getLength(); temp++) {
				        Node nNode = nList.item(temp);
				        System.out.println("\nCurrent Element :" + nNode.getNodeName());
				        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				            Element eElement = (Element) nNode;
				            
				            System.out.println("Ref id: "+ eElement.getAttribute("reference"));  
				    		System.out.println("Acc Name: "+ eElement.getElementsByTagName("accountNumber").item(0).getTextContent());  
				    		System.out.println("Desc Name: "+ eElement.getElementsByTagName("description").item(0).getTextContent());  
				    		System.out.println("s b : "+ eElement.getElementsByTagName("startBalance").item(0).getTextContent());  
				    		System.out.println("M b: "+ eElement.getElementsByTagName("mutation").item(0).getTextContent());  
				    		System.out.println("e b : "+ eElement.getElementsByTagName("endBalance").item(0).getTextContent());
				            
				            
				    		
				    		StatementModel sModel = new StatementModel(Long.parseLong(eElement.getAttribute("reference").toString()),
				    				eElement.getElementsByTagName("accountNumber").item(0).getTextContent(),
				    				eElement.getElementsByTagName("description").item(0).getTextContent(),
				    				Float.parseFloat(eElement.getElementsByTagName("startBalance").item(0).getTextContent()),
				    				Float.parseFloat(eElement.getElementsByTagName("mutation").item(0).getTextContent()),
				    				Float.parseFloat(eElement.getElementsByTagName("endBalance").item(0).getTextContent()));
				    		xmlStatemts.add(sModel);
				            
				            
				             }
				        
				    }
				    
				    return xmlStatemts;
				 //  CSVHelper.failedRecord(xmlStatemts);
				    } catch (Exception e) {
				    e.printStackTrace();
				    }
				return xmlStatemts;
}

	
	
	public  static List<StatementModel> failedRecord(List<StatementModel>  smodelsList) throws IOException
	
	{
		// failed records serch
		List<StatementModel> smodelsListDup = new ArrayList<StatementModel>();

		Set<StatementModel> smodel = new HashSet<StatementModel>();

		for (StatementModel sm : smodelsList) {
			System.out.println((sm.getStartBalance() + sm.getMutation()));
			System.out.println("----");

			System.out.println(sm.getEndBalance());
			if ((df.format(sm.getStartBalance() + sm.getMutation())).equals(df.format(sm.getEndBalance()))) {
				if (smodel.contains(sm)) {

					smodelsListDup.add(sm);
					System.out.println("Ref : " + sm.getReference() + " desc : " + sm.getDescription());
				} else {

					smodel.add(sm);

				}
			} else {
				smodelsListDup.add(sm);
				smodel.add(sm);
				System.out.println("in else " + sm.getReference() + " desc " + sm.getDescription());

			}

		}

		
		return smodelsListDup;
	}
	
	
	public static String writeCsv(List<StatementModel> smodelsListDup) throws IOException
	
	{
		Random rand = new Random(); 
		  
        int rand_int1 = rand.nextInt(1000); 
		String csv = "FailedRecordsOfcsv_"+rand_int1+".csv";
		CSVWriter writer1 = new CSVWriter(new FileWriter(csv));

		String [] failedRecords = "Reference#Description".split("#");

		writer1.writeNext(failedRecords);

		for(StatementModel val: smodelsListDup)
		{
			failedRecords = new String(String.valueOf(val.getReference())+"#"+val.getDescription()).split("#");
			
			writer1.writeNext(failedRecords);

		}
		writer1.close();
		
		return "Success";

	
	}
	
	
public static String writeXml(List<StatementModel> smodelsListDup) throws IOException
	
	{
		Random rand = new Random(); 
		  
        int rand_int1 = rand.nextInt(1000); 
		String xml = "FailedRecordsxml_"+rand_int1+".xml";
		 try {

		        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
		        DocumentBuilder build = dFact.newDocumentBuilder();
		        Document doc = build.newDocument();

		        Element root = doc.createElement("records");
		        doc.appendChild(root);

		      

		        for(int i=0; i<smodelsListDup.size(); i ++ ) {
		        	  Element Details = doc.createElement("record");
				        root.appendChild(Details);

		            Element name = doc.createElement("reference");
		            name.appendChild(doc.createTextNode(String.valueOf(smodelsListDup.get(i).getReference())));
		            Details.appendChild(name);

		            Element id = doc.createElement("description");
		            id.appendChild(doc.createTextNode(String.valueOf(smodelsListDup.get(i).getDescription())));
		            Details.appendChild(id);

		        }


		         // Save the document to the disk file
		        TransformerFactory tranFactory = TransformerFactory.newInstance();
		        Transformer aTransformer = tranFactory.newTransformer();

		        // format the XML nicely
		        aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

		        aTransformer.setOutputProperty(
		                "{http://xml.apache.org/xslt}indent-amount", "4");
		        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");



		        DOMSource source = new DOMSource(doc);
		        try {
		            FileWriter fos = new FileWriter(xml);
		            StreamResult result = new StreamResult(fos);
		            aTransformer.transform(source, result);

		        } catch (IOException e) {

		            e.printStackTrace();
		        }



		    } catch (TransformerException ex) {
		        System.out.println("Error outputting document");

		    } catch (ParserConfigurationException ex) {
		        System.out.println("Error building document");
		    }
		 return "success";
	
	}

	
}