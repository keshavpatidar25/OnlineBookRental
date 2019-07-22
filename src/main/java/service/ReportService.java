package service;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.RequestDao;
import dto.Book;


/**
 * The Class ReportService.
 */
public class ReportService {
	
	/**Logger Object for logging purpose */
	private static final Logger logger = Logger.getLogger(ReportService.class);
	
	/** The iText document which will act as PDF document. */
	private Document document=new Document(PageSize.A4.rotate());
	
	/** The heading color. */
	private BaseColor headingColor = BaseColor.BLACK; 
	
	/**
	 * Gets the report.
	 *
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @param os the Output Stream on which PDF data will be written
	 * @return the report data
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReport(String fromDate, String toDate, OutputStream os) throws IOException, Exception{
		logger.info("Generating Report");
		PdfPTable pdfTable=null;
		PdfWriter.getInstance(document, os);
		List<Map<String, Object>> reportList = new RequestDao().getReport(fromDate, toDate);
		Iterator<Map<String, Object>> it = reportList.iterator();
		
		
		try{
			pdfTable=generatePdfTable(it);
			document.open();
			addDatesToPdf(document, fromDate, toDate);
			document.add(pdfTable);
		}finally{
			if(document!=null)
				document.close();
		}
		
		return reportList;
	}
	
	/**
	 * Gets the report on basis of title.
	 * @param book1 the book which contains title on whose basis report is to be generated
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @param os the Output Stream on which PDF data will be written
	 * @return the report data on basis of title
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReportByTitle(Book book1, String fromDate, String toDate, OutputStream os) throws IOException, Exception{
		logger.info("Generating Report By Title");
		PdfPTable pdfTable=null;
		PdfWriter.getInstance(document, os);
		List<Map<String, Object>> reportList = new RequestDao().getReportByTitle(book1,fromDate, toDate);
		Iterator<Map<String, Object>> it = reportList.iterator();
		
		try{
			pdfTable= generatePdfTable(it);
			document.open();
			addDatesToPdf(document, fromDate, toDate);
			document.add(new Paragraph("Report on Basis of Book Title : "+book1.getBookTitle()+"\n\n"));
			document.add(pdfTable);
		}finally{
			if(document!=null)
				document.close();
		}
		return reportList;
	}
	
	/**
	 * Gets the report on basis of author.
	 * @param book1 the book which contains author on whose basis report is to be generated
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @param os the Output Stream on which PDF data will be written
	 * @return the report data on basis of author
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReportByAuthor(Book book1, String fromDate, String toDate, OutputStream os) throws IOException, Exception{
		logger.info("Generating Report By Author");
		Book book=null;
		PdfPTable pdfTable=null;
		PdfWriter.getInstance(document, os);
		
		List<Map<String, Object>> reportList = new RequestDao().getReportByAuthor(book1, fromDate, toDate);
		Iterator<Map<String, Object>> it = reportList.iterator();
		Map<String,Object> map=null;
		try{
			pdfTable= new PdfPTable(9);
			/** define table header cell */
			PdfPCell cell = new PdfPCell();
			Font headerFont = new Font(FontFamily.TIMES_ROMAN);
			headerFont.setColor(BaseColor.WHITE);
			headerFont.setStyle(Font.BOLD);
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header */
			cell.setPhrase(new Phrase("Book No", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Title", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header */
			cell.setPhrase(new Phrase("Category", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Issue Pending Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Issue Cancelled Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Issue Closed Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Return Pending Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Return Cancelled Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Return Closed Count", headerFont));
			pdfTable.addCell(cell);
			while(it.hasNext()){
				map=it.next();
				book=(Book)map.get("book");
				pdfTable.addCell(String.valueOf(book.getBookNo()));
				pdfTable.addCell(book.getBookTitle());
				pdfTable.addCell(book.getBookCategory());
				pdfTable.addCell(map.get("issuePending").toString());
				pdfTable.addCell(map.get("issueCancelled").toString());
				pdfTable.addCell(map.get("issueClosed").toString());
				pdfTable.addCell(map.get("returnPending").toString());
				pdfTable.addCell(map.get("returnCancelled").toString());
				pdfTable.addCell(map.get("returnClosed").toString());
			}
			document.open();
			addDatesToPdf(document, fromDate, toDate);
			document.add(new Paragraph("Report on Basis of Book Author : "+book1.getBookAuthor()+"\n\n"));
			document.add(pdfTable);
		} finally{	
			document.close();
		}
		return reportList;
	}
	
	/**
	 * Gets the report on basis of category.
	 *
	 * @param book1 the book which contains category on whose basis report is to be generated
	 * @param fromDate the date from which report is to be generated
	 * @param toDate the date to which report is to be generated
	 * @param os the Output Stream on which PDF data will be written
	 * @return the report data on basis of category
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws Exception the exception
	 */
	public List<Map<String,Object>> getReportByCategory(Book book1, String fromDate, String toDate, OutputStream os) throws IOException, Exception{
		logger.info("Generating Report By Category");
		Book book=null;
		PdfPTable pdfTable=null;
		PdfWriter.getInstance(document, os);
		
		List<Map<String, Object>> reportList = new RequestDao().getReportByCategory(book1, fromDate, toDate);
		Iterator<Map<String, Object>> it = reportList.iterator();
		Map<String,Object> map=null;
		try{
			pdfTable= new PdfPTable(9);
			/** define table header cell*/
			PdfPCell cell = new PdfPCell();
			Font headerFont = new Font(FontFamily.TIMES_ROMAN);
			headerFont.setColor(BaseColor.WHITE);
			headerFont.setStyle(Font.BOLD);
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Book No", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Title", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Author", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Issue Pending Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Issue Cancelled Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Issue Closed Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Return Pending Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Return Cancelled Count", headerFont));
			pdfTable.addCell(cell);
			cell = new PdfPCell();
			cell.setBackgroundColor(headingColor);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPadding(5);
			/** write table header*/ 
			cell.setPhrase(new Phrase("Return Closed Count", headerFont));
			pdfTable.addCell(cell);
			while(it.hasNext()){
				map=it.next();
				book=(Book)map.get("book");
				pdfTable.addCell(String.valueOf(book.getBookNo()));
				pdfTable.addCell(book.getBookTitle());
				pdfTable.addCell(book.getBookAuthor());
				pdfTable.addCell(map.get("issuePending").toString());
				pdfTable.addCell(map.get("issueCancelled").toString());
				pdfTable.addCell(map.get("issueClosed").toString());
				pdfTable.addCell(map.get("returnPending").toString());
				pdfTable.addCell(map.get("returnCancelled").toString());
				pdfTable.addCell(map.get("returnClosed").toString());
			}
			document.open();
			addDatesToPdf(document, fromDate, toDate);
			document.add(new Paragraph("Report on Basis of Book Category : "+book1.getBookCategory()+"\n\n"));
			document.add(pdfTable);
		}finally{	
			if(document!=null)
				document.close();
		}
		return reportList;
	}
	
	/**
	 * Generate pdf table.
	 *
	 * @param it the iterator which contains all details of pdf report to be generated
	 * @return the pdf p table which will be added to document
	 * @throws Exception the exception
	 */
	protected PdfPTable generatePdfTable(Iterator<Map<String, Object>> it) throws Exception{
		logger.info("Generating PdfTable in case of Generating report or Generating report by Title.");
		PdfPTable pdfTable= new PdfPTable(10);
		Map<String,Object> map=null;
		Book book=null;
		/** define table header cell*/
		PdfPCell cell = new PdfPCell();
		Font headerFont = new Font(FontFamily.TIMES_ROMAN);
		headerFont.setColor(BaseColor.WHITE);
		headerFont.setStyle(Font.BOLD);
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Book No", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Title", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Author", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Category", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Issue Pending Count", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Issue Cancelled Count", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Issue Closed Count", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Return Pending Count", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Return Cancelled Count", headerFont));
		pdfTable.addCell(cell);
		cell = new PdfPCell();
		cell.setBackgroundColor(headingColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(5);
		/** write table header*/ 
		cell.setPhrase(new Phrase("Return Closed Count", headerFont));
		pdfTable.addCell(cell);
		
		while(it.hasNext()){
			map=it.next();
			book=(Book)map.get("book");
			pdfTable.addCell(String.valueOf(book.getBookNo()));
			pdfTable.addCell(book.getBookTitle());
			pdfTable.addCell(book.getBookAuthor());
			pdfTable.addCell(book.getBookCategory());
			pdfTable.addCell(map.get("issuePending").toString());
			pdfTable.addCell(map.get("issueCancelled").toString());
			pdfTable.addCell(map.get("issueClosed").toString());
			pdfTable.addCell(map.get("returnPending").toString());
			pdfTable.addCell(map.get("returnCancelled").toString());
			pdfTable.addCell(map.get("returnClosed").toString());
		}
		return pdfTable;
	}
	
	/**
	 * Converts the dates into dd-MMM-yyyy format. Then adds the dates to pdf.
	 *
	 * @param document the pdf document (iText document)
	 * @param fromDate the date from which report is to generated 
	 * @param toDate the the date to which report is to generated
	 * @throws Exception the exception
	 */
	protected void addDatesToPdf(Document document,String fromDate, String toDate) throws Exception {
		logger.info("Adding Dates to Pdf");
		Date from = (new SimpleDateFormat("yyyy-MM-dd").parse(fromDate));
		fromDate = new SimpleDateFormat("dd-MMM-yyyy").format(from).toString();
		Date to = (new SimpleDateFormat("yyyy-MM-dd").parse(toDate));
		toDate = new SimpleDateFormat("dd-MMM-yyyy").format(to).toString();
		document.add(new Paragraph(new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date()).toString()+"\n\n"));
		document.add(new Paragraph("Report From : "+fromDate+"\t To :"+toDate+"\n\n"));
		
	}
}
