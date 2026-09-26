package com.Mboacare.Mboacare.services;
import com.Mboacare.Mboacare.entities.Consultation;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
@Service
public class PdfService {
    public byte[] genererPdfConsultation(Consultation consultation) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        document.add(new Paragraph("Compte-rendu de Consultation"));
        document.add(new Paragraph("Date: " + consultation.getDateConsultation()));
        document.add(new Paragraph("Motif: " + consultation.getMotif()));
        document.add(new Paragraph("Diagnostic: " + consultation.getDiagnostic()));
        document.add(new Paragraph("Observations: " + consultation.getObservations()));
        document.close();
        return out.toByteArray();
    }
}
