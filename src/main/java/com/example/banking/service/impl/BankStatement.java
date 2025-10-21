package com.example.banking.service.impl;

import com.example.banking.dto.EmailDetails;
import com.example.banking.entity.Transaction;
import com.example.banking.entity.User;
import com.example.banking.repository.TransactionRepository;
import com.example.banking.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final String FILE = "C:\\Users\\DELL\\Downloads\\MyStatement.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        // Fetch user
        User user = userRepository.findByAccountNumber(accountNumber);
        if (user == null) {
            log.error("User not found for account number: {}", accountNumber);
            throw new RuntimeException("User not found for account number: " + accountNumber);
        }

        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt() != null)
                .filter(transaction ->
                        (transaction.getCreatedAt().isEqual(start) || transaction.getCreatedAt().isAfter(start)) &&
                                (transaction.getCreatedAt().isBefore(end) || transaction.getCreatedAt().isEqual(end))
                )
                .toList();

        try {
            generatePDF(user, transactionList, start, end);
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
        }

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Your Account Statement")
                .messageBody("Dear " + user.getFirstName() + ",\n\nPlease find attached your bank statement.\n\nThank you.")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }

    private void generatePDF(User user, List<Transaction> transactions,
                             LocalDate startDate, LocalDate endDate)
            throws DocumentException, FileNotFoundException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();

        Paragraph bankName = new Paragraph("Banking Application",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLUE));
        bankName.setAlignment(Element.ALIGN_CENTER);
        document.add(bankName);

        Paragraph bankAddress = new Paragraph("202, Nachiketa Building, Andheri West",
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        bankAddress.setAlignment(Element.ALIGN_CENTER);
        document.add(bankAddress);
        document.add(Chunk.NEWLINE);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.addCell(getCell("Customer Name: " + user.getFirstName() + " " + user.getLastName(), PdfPCell.ALIGN_LEFT));
        infoTable.addCell(getCell("Account Number: " + user.getAccountNumber(), PdfPCell.ALIGN_RIGHT));
        infoTable.addCell(getCell("Start Date: " + startDate, PdfPCell.ALIGN_LEFT));
        infoTable.addCell(getCell("End Date: " + endDate, PdfPCell.ALIGN_RIGHT));

        document.add(infoTable);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell(getHeaderCell("Date"));
        table.addCell(getHeaderCell("Type"));
        table.addCell(getHeaderCell("Amount"));
        table.addCell(getHeaderCell("Status"));

        for (Transaction t : transactions) {
            table.addCell(getCell(t.getCreatedAt().toString(), PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(t.getTransactionType(), PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(t.getAmount().toString(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(t.getStatus(), PdfPCell.ALIGN_CENTER));
        }

        document.add(table);
        document.close();
        log.info("PDF Bank Statement generated successfully at {}", FILE);
    }

    private PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(8f);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell getHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10f);
        return cell;
    }
}
