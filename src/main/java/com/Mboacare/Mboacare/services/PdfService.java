package com.Mboacare.Mboacare.services;

import com.Mboacare.Mboacare.entities.Consultation;
import com.Mboacare.Mboacare.entities.Utilisateur;
import com.Mboacare.Mboacare.repositories.UtilisateurRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private final UtilisateurRepository utilisateurRepository;

    public PdfService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public byte[] genererPdfConsultation(Consultation consultation) throws DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        // Récupérer les infos du médecin et du patient
        Utilisateur medecin = utilisateurRepository.findById(consultation.getIdMedecin()).orElse(null);
        Utilisateur patient = utilisateurRepository.findById(consultation.getIdPatient()).orElse(null);

        // Polices
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.decode("#1a56db")); 
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.GRAY); 
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, Color.BLACK);
        Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
        Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.decode("#1a56db"));
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font normalBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);

        // 1. En-tête (Logo et Médecin)
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        try { headerTable.setWidths(new float[]{60f, 40f}); } catch (Exception e) {}

        // Infos Médecin
        PdfPCell doctorCell = new PdfPCell();
        doctorCell.setBorder(Rectangle.NO_BORDER);
        if (medecin != null) {
            doctorCell.addElement(new Paragraph("Dr. " + medecin.getPrenom() + " " + medecin.getNom(), headerFont));
            doctorCell.addElement(new Paragraph(medecin.getSpecialite() != null ? medecin.getSpecialite().toUpperCase() : "MÉDECIN", subHeaderFont));
            doctorCell.addElement(new Paragraph("ONMC : " + (medecin.getNumeroOrdre() != null ? medecin.getNumeroOrdre() : "Non renseigné"), subHeaderFont));
            doctorCell.addElement(new Paragraph("Tél : " + (medecin.getTelephone() != null ? medecin.getTelephone() : "Non renseigné"), subHeaderFont));
            doctorCell.addElement(new Paragraph(medecin.getLieuExercice() != null ? medecin.getLieuExercice() : "Cabinet Médical MboaCare", subHeaderFont));
        } else {
            doctorCell.addElement(new Paragraph("Médecin", headerFont));
        }
        headerTable.addCell(doctorCell);

        // Logo MboaCare
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        
        try {
            org.springframework.core.io.ClassPathResource imgFile = new org.springframework.core.io.ClassPathResource("static/logo.jpg");
            try (java.io.InputStream is = imgFile.getInputStream()) {
                byte[] bytes = is.readAllBytes();
                
                // Filigrane (Watermark)
                Image watermark = Image.getInstance(bytes);
                watermark.setAbsolutePosition(100, 250);
                watermark.scaleAbsolute(400, 400);
                
                // Opacité (transparence) pour le filigrane (via PdfGState)
                PdfGState gstate = new PdfGState();
                gstate.setFillOpacity(0.05f);
                PdfContentByte over = writer.getDirectContentUnder();
                over.saveState();
                over.setGState(gstate);
                over.addImage(watermark);
                over.restoreState();
                
                // Logo en haut à droite (cercle)
                PdfContentByte cb = writer.getDirectContent();
                PdfTemplate template = cb.createTemplate(60, 60);
                template.ellipse(0, 0, 60, 60);
                template.clip();
                template.newPath();
                
                Image logoRaw = Image.getInstance(bytes);
                float imgW = logoRaw.getWidth();
                float imgH = logoRaw.getHeight();
                float scale = Math.max(60f / imgW, 60f / imgH);
                float w = imgW * scale;
                float h = imgH * scale;
                float x = (60f - w) / 2f;
                float y = (60f - h) / 2f;
                
                template.addImage(logoRaw, w, 0, 0, h, x, y);
                
                Image logo = Image.getInstance(template);
                logo.setAlignment(Element.ALIGN_RIGHT);
                logoCell.addElement(logo);
            }
        } catch (Exception e) {
            // Fallback si l'image n'est pas trouvée
            Paragraph mboa = new Paragraph("MboaCare", titleFont);
            mboa.setAlignment(Element.ALIGN_RIGHT);
            logoCell.addElement(mboa);
        }
        
        Paragraph dh = new Paragraph("DIGITAL HEALTH", subtitleFont);
        dh.setAlignment(Element.ALIGN_RIGHT);
        logoCell.addElement(dh);
        headerTable.addCell(logoCell);

        document.add(headerTable);

        // Ligne de séparation
        document.add(new Chunk("\n"));
        document.add(new com.lowagie.text.pdf.draw.LineSeparator(2f, 100f, Color.decode("#1a56db"), Element.ALIGN_CENTER, -5f));
        document.add(new Chunk("\n\n"));

        // 2. Titre du document
        Paragraph docTitle = new Paragraph("COMPTE-RENDU DE CONSULTATION", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK));
        docTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(docTitle);
        document.add(new Chunk("\n\n"));

        // 3. Infos du Patient et Date (Dans un cadre léger)
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        
        PdfPCell patientCell = new PdfPCell();
        patientCell.setBorder(Rectangle.NO_BORDER);
        if (patient != null) {
            patientCell.addElement(new Paragraph("Patient : " + patient.getPrenom() + " " + patient.getNom(), normalBoldFont));
            patientCell.addElement(new Paragraph("ID Patient : " + patient.getId(), subHeaderFont));
        } else {
            patientCell.addElement(new Paragraph("Patient n°" + consultation.getIdPatient(), normalBoldFont));
        }
        infoTable.addCell(patientCell);

        PdfPCell dateCell = new PdfPCell();
        dateCell.setBorder(Rectangle.NO_BORDER);
        Paragraph pDate = new Paragraph("Date : " + consultation.getDateConsultation(), normalBoldFont);
        pDate.setAlignment(Element.ALIGN_RIGHT);
        dateCell.addElement(pDate);
        if (consultation.getHeureConsultation() != null) {
            Paragraph pTime = new Paragraph("Heure : " + consultation.getHeureConsultation().toString().substring(0, 5), subHeaderFont);
            pTime.setAlignment(Element.ALIGN_RIGHT);
            dateCell.addElement(pTime);
        }
        infoTable.addCell(dateCell);

        document.add(infoTable);
        document.add(new Chunk("\n\n"));

        // 4. Contenu de la consultation
        document.add(createSection("Motif de la consultation", consultation.getMotif(), sectionTitleFont, normalFont));
        document.add(createSection("Diagnostic", consultation.getDiagnostic() != null ? consultation.getDiagnostic() : "Non renseigné", sectionTitleFont, normalFont));
        document.add(createSection("Observations & Notes Cliniques", consultation.getObservations() != null ? consultation.getObservations() : "Aucune observation", sectionTitleFont, normalFont));

        // 5. Bas de page et Signature
        document.add(new Chunk("\n\n\n\n"));
        PdfPTable footerTable = new PdfPTable(2);
        footerTable.setWidthPercentage(100);
        
        PdfPCell leftFooter = new PdfPCell(new Paragraph("Consultation n°" + consultation.getIdConsultation() + "\nDocument généré électroniquement.", subHeaderFont));
        leftFooter.setBorder(Rectangle.NO_BORDER);
        leftFooter.setVerticalAlignment(Element.ALIGN_BOTTOM);
        footerTable.addCell(leftFooter);

        PdfPCell rightFooter = new PdfPCell();
        rightFooter.setBorder(Rectangle.NO_BORDER);
        Paragraph sigLine = new Paragraph("___________________________", normalFont);
        sigLine.setAlignment(Element.ALIGN_RIGHT);
        rightFooter.addElement(sigLine);
        Paragraph sigTxt = new Paragraph("Signature & Cachet", normalBoldFont);
        sigTxt.setAlignment(Element.ALIGN_RIGHT);
        rightFooter.addElement(sigTxt);
        footerTable.addCell(rightFooter);

        document.add(footerTable);

        document.close();
        return out.toByteArray();
    }

    private Paragraph createSection(String title, String content, Font titleFont, Font contentFont) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(title + "\n", titleFont));
        
        Paragraph contentPara = new Paragraph(content, contentFont);
        contentPara.setIndentationLeft(10f); // Légère indentation pour le texte
        contentPara.setSpacingAfter(15f);
        p.add(contentPara);
        
        return p;
    }
}
