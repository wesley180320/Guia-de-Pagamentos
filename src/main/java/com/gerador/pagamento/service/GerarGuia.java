package com.gerador.pagamento.service;

import Util.CPFUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;

public class GerarGuia {

    private static final String TXID = "TX1234567890";
    private static final String OUTPUT_FOLDER = "pdfs";
    private static final String OUTPUT_FILE = OUTPUT_FOLDER + "/guia.pdf";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static byte[] gerarGuiaPdf(
            String proprietario,
            String documento,
            String endereco,
            BigDecimal valor,
            LocalDate vencimento,
            String chavePix,
            String nomeRecebedor,
            String cidadeRecebedor
    ) {
        try {
            File dir = new File(OUTPUT_FOLDER);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            documento = CPFUtils.formatarCPF(documento);

            String payloadPix = gerarPayloadPix(chavePix, nomeRecebedor, cidadeRecebedor, valor);
            BufferedImage qr = gerarQRCode(payloadPix, 200);
            BufferedImage barcode = gerarQRCode("12345678901234567890", 400);

            byte[] pdfByte = criarPdf(
                    OUTPUT_FILE,
                    proprietario,
                    documento,
                    endereco,
                    valor,
                    vencimento,
                    "12345678901234567890",
                    payloadPix,
                    qr,
                    barcode
            );

            return pdfByte;
        } catch (IOException e) {
            throw new PdfGenerationException("Erro ao salvar o PDF da guia de pagamento", e);
        } catch (WriterException e) {
            throw new QRCodeGenerationException("Erro ao gerar o QR Code", e);
        } catch (DocumentException e) {
            throw new PdfGenerationException("Erro ao escrever no documento PDF", e);
        } catch (Exception e) {
            throw new PdfGenerationException("Erro inesperado ao gerar a guia", e);
        }
    }

    private static byte[] criarPdf(
            String path,
            String proprietario,
            String documento,
            String endereco,
            BigDecimal valor,
            LocalDate vencimento,
            String codigoBarras,
            String payloadPix,
            BufferedImage qrImg,
            BufferedImage barcodeImg
    ) throws IOException, DocumentException {

        try (FileOutputStream fos = new FileOutputStream(path)) {

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter writer = PdfWriter.getInstance(doc, byteArray);
            PdfWriter.getInstance(doc, fos);
            doc.open();

            adicionarCabecalho(doc);
            adicionarInfoPrincipal(doc, proprietario, documento, endereco, valor, vencimento);
            adicionarCodigos(doc, barcodeImg, qrImg, payloadPix);

            doc.close();
            return byteArray.toByteArray();
        }
    }

    private static void adicionarCabecalho(Document doc) throws DocumentException {
        Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph pTitulo = new Paragraph("GUIA DE PAGAMENTO (FICTÍCIA)", titulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        pTitulo.setSpacingAfter(12);
        doc.add(pTitulo);
    }

    private static void adicionarInfoPrincipal(Document doc,
                                               String proprietario,
                                               String documento,
                                               String endereco,
                                               BigDecimal valor,
                                               LocalDate vencimento) throws DocumentException {

        Font normal = new Font(Font.HELVETICA, 11, Font.ITALIC);
        Font negrito = new Font(Font.HELVETICA, 11, Font.BOLD);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{60, 40});

        PdfPCell c1 = new PdfPCell();
        c1.setPadding(8);
        c1.addElement(new Paragraph("Proprietário: " + proprietario, normal));
        c1.addElement(new Paragraph("Documento: " + documento, normal));
        c1.addElement(new Paragraph("Endereço: " + endereco, normal));
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell();
        c2.setPadding(8);
        c2.addElement(new Paragraph("Vencimento: " + DATE_FORMAT.format(vencimento), negrito));
        c2.addElement(new Paragraph("Valor: R$ " + valor.toPlainString(), negrito));
        table.addCell(c2);

        doc.add(table);
        doc.add(new Paragraph("\n"));
    }

    private static void adicionarCodigos(Document doc,
                                         BufferedImage barcodeImg,
                                         BufferedImage qrImg,
                                         String payloadPix) throws IOException, DocumentException {

        Font negrito = new Font(Font.HELVETICA, 11, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11, Font.ITALIC);

        PdfPTable segundaTabela = new PdfPTable(2);
        segundaTabela.setWidthPercentage(100);
        segundaTabela.setSpacingAfter(6);

        segundaTabela.addCell(criarCelulaTexto("Código de Barras:", negrito));
        segundaTabela.addCell(criarCelulaTexto("QR Code:", negrito));

        doc.add(segundaTabela);

        PdfPTable tableImg = new PdfPTable(2);
        tableImg.setWidthPercentage(100);
        tableImg.setSpacingBefore(10);
        tableImg.setSpacingAfter(10);

        tableImg.addCell(criarCelulaImagem(barcodeImg));
        tableImg.addCell(criarCelulaImagem(qrImg));

        doc.add(tableImg);

        Paragraph pixParagraph = new Paragraph("Código PIX: " + payloadPix, normal);
        pixParagraph.setSpacingBefore(10);
        pixParagraph.setSpacingAfter(6);
        doc.add(pixParagraph);
    }

    private static PdfPCell criarCelulaTexto(String texto, Font fonte) {
        PdfPCell cell = new PdfPCell(new Paragraph(texto, fonte));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private static PdfPCell criarCelulaImagem(BufferedImage img) throws IOException, BadElementException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "PNG", baos);
            Image image = Image.getInstance(baos.toByteArray());
            image.scaleAbsolute(150, 150);

            PdfPCell cell = new PdfPCell(image);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            return cell;
        }
    }

    public static String gerarPayloadPix(String chave, String nomeRecebedor, String cidade, BigDecimal valor) {
        String payloadFormatIndicator = tlv("00", "01");
        String pointOfInitiation = tlv("01", "12");
        String gui = tlv("00", "br.gov.bcb.pix");
        String chaveField = tlv("01", chave);
        String mai = tlv("26", gui + chaveField);
        String merchantCategoryCode = tlv("52", "0000");
        String transactionCurrency = tlv("53", "986"); // BRL
        String transactionAmount = tlv("54", formatarValor(valor));
        String countryCode = tlv("58", "BR");
        String merchantName = tlv("59", limitar(nomeRecebedor, 25));
        String merchantCity = tlv("60", limitar(cidade, 15));
        String addDataField = tlv("62", tlv("05", limitar(TXID, 25))); // TXID

        String semCRC = payloadFormatIndicator + pointOfInitiation + mai +
                merchantCategoryCode + transactionCurrency + transactionAmount +
                countryCode + merchantName + merchantCity + addDataField + "6304";

        String crc = crc16(semCRC).toUpperCase();
        return semCRC + crc;
    }

    private static String tlv(String id, String value) {
        String len = String.format("%02d", value.getBytes(StandardCharsets.UTF_8).length);
        return id + len + value;
    }

    public static String crc16(String payload) {
        int polynomial = 0x1021;
        int result = 0xFFFF;
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i)) & 1) == 1;
                boolean c15 = ((result >> 15) & 1) == 1;
                result <<= 1;
                if (c15 ^ bit) {
                    result ^= polynomial;
                }
                result &= 0xFFFF;
            }
        }
        return String.format("%04X", result);
    }

    private static BufferedImage gerarQRCode(String content, int size) throws WriterException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    private static String formatarValor(BigDecimal v) {
        return v.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private static String limitar(String s, int max) {
        if (s == null) return "";
        return (s.length() <= max) ? s : s.substring(0, max);
    }

    public static class PdfGenerationException extends RuntimeException {
        public PdfGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class QRCodeGenerationException extends RuntimeException {
        public QRCodeGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
