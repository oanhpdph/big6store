package com.poly.common;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.poly.entity.Bill;
import com.poly.entity.BillProduct;
import com.poly.entity.ProductDetailField;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.kernel.colors.DeviceGray.*;

@Service
public class SavePdf<T> {

    public void savePdf(HttpServletResponse response, List<T> listData, List<String> header) throws IOException {
        response.setContentType("application/pdf");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document doc = new Document(pdfDoc, new PageSize(595, 842));
        doc.setMargins(55, 15, 35, 15);
        ILineDrawer line = new SolidLine(2);
        line.setColor(ColorConstants.LIGHT_GRAY);

        LineSeparator tableEndSeparator = new LineSeparator(line);
        tableEndSeparator.setMarginTop(10);

        doc.add(createTable(listData, header));
        doc.add(tableEndSeparator);
        doc.close();
    }

    public Table createTable(List<T> listData, List<String> header) throws IOException {
        Table table = new Table(header.size()).useAllAvailableWidth();
        for (T temp : listData) {
            List<Object> listCell = new ArrayList<>();
            Class<?> tempClass = temp.getClass();
            Field[] fields = tempClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Cho phép truy cập các thuộc tính private
                Object value = null;
                try {
                    value = field.get(temp);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                listCell.add(value);
            }
            listCell.remove(listCell.size() - 1);
            addRow(listCell, table);
        }
        addHeader(header, table);
        return table;
    }

    public void addHeader(List<String> header, Table table) throws IOException {
        PdfFont font = PdfFontFactory.createFont("src/main/resources/static/admin/assets/vendor/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        for (String columnHeader : header) {

            Paragraph headerParagraph = new Paragraph(columnHeader)
                    .setFont(font)
                    .setFontSize(10);

            Cell headerCell = new Cell()
                    .add(headerParagraph)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                    .setPadding(8);

            table.addHeaderCell(headerCell);
        }
    }

    public void addRow(List<Object> content, Table table) throws IOException {
        PdfFont font = PdfFontFactory.createFont("src/main/resources/static/admin/assets/vendor/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        for (Object text : content) {
            Paragraph paragraph = new Paragraph(String.valueOf(text))
                    .setFont(font)
                    .setFontSize(10);
            Cell cell = new Cell()
                    .add(paragraph)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                    .setPaddingLeft(5)
                    .setPaddingTop(5)
                    .setPaddingRight(5)
                    .setPaddingBottom(5);
            table.addCell(cell);
        }
    }

    static Cell getBillingandShippingCell(String textValue) throws IOException {
        PdfFont font = PdfFontFactory.createFont("src/main/resources/static/admin/assets/vendor/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        return new Cell().add(new Paragraph(textValue).setFont(font)).setFontSize(14f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10fLeft(String value, Boolean isBold) throws IOException {
        PdfFont font = PdfFontFactory.createFont("src/main/resources/static/admin/assets/vendor/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        Cell cell = new Cell().add(new Paragraph(value).setFont(font)).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? cell.setBold() : cell;
    }

    static Cell getHeaderTextCellValue(String textValue) throws IOException {
        PdfFont font = PdfFontFactory.createFont("src/main/resources/static/admin/assets/vendor/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        return new Cell().add(new Paragraph(textValue).setFont(font)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    public byte[] generatePdf(Bill bill) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
            float fiveCol = 114f;
            float threecol = 190f;
            float twocol = 285f;
            float twocol150 = twocol + 150f;
            float twocolumnWidth[] = {twocol150, twocol};
            float fullWidth[] = {threecol * 3};
            float fiveColWidth[] = {fiveCol, fiveCol, fiveCol, fiveCol, fiveCol};
            BigDecimal totalSum = BigDecimal.valueOf(0);

            Paragraph onesp = new Paragraph("\n");
            PdfFont font = PdfFontFactory.createFont("src/main/resources/static/admin/assets/vendor/fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
            DecimalFormat decimalFormat1 = new DecimalFormat("#,###");

            Table table = new Table(twocolumnWidth);
            table.addCell(new Cell().add(new Paragraph("Hóa đơn").setFontSize(30).setFont(font)).setBorder(Border.NO_BORDER).setBold());
            Table nestedtabe = new Table(new float[]{twocol / 2, twocol / 2});
            nestedtabe.addCell(getHeaderTextCellValue("Mã hóa đơn"));
            nestedtabe.addCell(getHeaderTextCellValue(bill.getCode()));
            nestedtabe.addCell(getHeaderTextCellValue("Ngày tạo"));
            nestedtabe.addCell(getHeaderTextCellValue(String.valueOf(bill.getCreateDate())));

            table.addCell(new Cell().add(nestedtabe).setBorder(Border.NO_BORDER));
            Border gb = new SolidBorder(GRAY, 2f);
            Table divider = new Table(fullWidth);
            divider.setBorder(gb);

            document.add(table);
            document.add(onesp);
            document.add(divider);
            document.add(onesp);

            Table twoColTable0 = new Table(twocolumnWidth);
            twoColTable0.addCell(getBillingandShippingCell("Thông tin Cửa hàng"));
            twoColTable0.addCell(getBillingandShippingCell(""));
            document.add(twoColTable0.setMarginBottom(12f));
            document.add(new Paragraph("Cửa hàng: Cửa hàng Điện tử Big6 Store.").setFont(font));
            document.add(new Paragraph("Địa chỉ: 109 P. Đồng Giao, Bắc Sơn, Tam Điệp, Ninh Bình.").setFont(font));
            document.add(new Paragraph("Số điện thoại: 0945643297.").setFont(font));


            Table twoColTable = new Table(twocolumnWidth);
            twoColTable.addCell(getBillingandShippingCell("Thông tin mua hàng"));
            twoColTable.addCell(getBillingandShippingCell(""));

            document.add(twoColTable.setMarginBottom(12f));

            Table twoColTable2 = new Table(twocolumnWidth);
            twoColTable2.addCell(getCell10fLeft("Người nhận", true));
            twoColTable2.addCell(getCell10fLeft("SĐT nhận hàng", true));
            twoColTable2.addCell(getCell10fLeft(bill.getDeliveryNotes().get(0).getReceived(), false));
            twoColTable2.addCell(getCell10fLeft(bill.getDeliveryNotes().get(0).getReceiverPhone(), false));

            if (bill.getBillProducts() != null) {
                for (BillProduct billProduct : bill.getBillProducts()) {
                    totalSum = totalSum.add(billProduct.getPrice().subtract(billProduct.getReducedMoney()).multiply(BigDecimal.valueOf(billProduct.getQuantity())));
                }
            }
            twoColTable2.addCell(getCell10fLeft("Phí vận chuyển", true));
            twoColTable2.addCell(getCell10fLeft("Voucher", true));
            twoColTable2.addCell(getCell10fLeft(decimalFormat1.format(bill.getDeliveryNotes().get(0).getDeliveryFee()) + " VNĐ", false));
            twoColTable2.addCell(getCell10fLeft(decimalFormat1.format(bill.getVoucherValue()) + " VNĐ", false));


            twoColTable2.addCell(getCell10fLeft("Email mua hàng", true));
            twoColTable2.addCell(getCell10fLeft("Địa chỉ nhận", true));
            twoColTable2.addCell(getCell10fLeft(bill.getDeliveryNotes().get(0).getReceivedEmail(), false));
            twoColTable2.addCell(getCell10fLeft(bill.getDeliveryNotes().get(0).getReceivingAddress(), false));


            twoColTable2.addCell(getCell10fLeft("Phương thức thanh toán", true));
            twoColTable2.addCell(getCell10fLeft("Tình trạng", true));
            twoColTable2.addCell(getCell10fLeft(bill.getPaymentMethod().getId() == 1 ? "Tiền mặt" : "VNPay", false));
            twoColTable2.addCell(getCell10fLeft(bill.getPaymentStatus() == 1 ? "Đã thanh toán" : "Chưa thanh toán", false));

            twoColTable2.addCell(getCell10fLeft("Tổng tiền phải trả", true));
            twoColTable2.addCell(getCell10fLeft(decimalFormat1.format(totalSum.subtract(bill.getVoucherValue()).add(bill.getDeliveryNotes().get(0).getDeliveryFee())) + " VNĐ", false));
            twoColTable2.addCell(getCell10fLeft("", true));
            twoColTable2.addCell(getCell10fLeft("", false));


            document.add(twoColTable2.setMarginBottom(12f));

            Border gb2 = new DashedBorder(GRAY, 0.5f);
            Border gb3 = new SolidBorder(GRAY, 0.5f);

            Table divider2 = new Table(fullWidth);

            document.add(divider2.setBorder(gb2));

            Paragraph productPara = new Paragraph("Thông tin sản phẩm").setFont(font);

            document.add(productPara.setBold());
            Table productTable = new Table(fiveColWidth);
            productTable.setBackgroundColor(BLACK, 0.7f);

            productTable.addCell(new Cell().add(new Paragraph("Sản phẩm").setFont(font).setFontColor(WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold()));
            productTable.addCell(new Cell().add(new Paragraph("Giá gốc(VNĐ)").setFont(font).setFontColor(WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold()));
            productTable.addCell(new Cell().add(new Paragraph("Được giảm(VNĐ)").setFont(font).setFontColor(WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold()));
            productTable.addCell(new Cell().add(new Paragraph("Số lượng").setFont(font).setFontColor(WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold()));
            productTable.addCell(new Cell().add(new Paragraph("Thành tiền(VNĐ)").setFont(font).setFontColor(WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER).setBold()));

            document.add(productTable);

            Table productTable2 = new Table(fiveColWidth);

            List<String> nameProduct;
            if (bill.getBillProducts() != null) {
                for (BillProduct billProduct : bill.getBillProducts()) {
                    nameProduct = new ArrayList<>();
                    nameProduct.add(billProduct.getProduct().getProduct().getNameProduct() + billProduct.getProduct().getProduct().getSku());
                    nameProduct.add("[ ");
                    for (ProductDetailField productFieldValue : billProduct.getProduct().getFieldList()) {
                        nameProduct.add(productFieldValue.getValue());
                    }
                    nameProduct.add(" ]");
                    productTable2.addCell(new Cell().add(new Paragraph(String.join(" ", nameProduct)).setFont(font)));

                    productTable2.addCell(new Cell().add(new Paragraph(decimalFormat1.format(billProduct.getPrice())).setFont(font)));
                    productTable2.addCell(new Cell().add(new Paragraph(decimalFormat1.format(billProduct.getReducedMoney())).setFont(font)));
                    productTable2.addCell(new Cell().add(new Paragraph(String.valueOf(billProduct.getQuantity())).setFont(font)));
                    productTable2.addCell(new Cell().add(new Paragraph(decimalFormat1.format(billProduct.getPrice().subtract(billProduct.getReducedMoney()).multiply(BigDecimal.valueOf(billProduct.getQuantity())))).setFont(font)));
                }
            }
            document.add(productTable2.setMarginBottom(20f));
            float onetwo[] = {threecol + 125f, threecol * 2};
            Table totalTable = new Table(onetwo);
            totalTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            totalTable.addCell(new Cell().add(divider2).setBorder(Border.NO_BORDER));
            document.add(totalTable);


            Table totalTable2 = new Table(fiveColWidth);
            totalTable2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER).setMarginLeft(10f));
            totalTable2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER).setMarginLeft(10f));
            totalTable2.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER).setMarginLeft(10f));
            totalTable2.addCell(new Cell().add(new Paragraph("Tổng tiền sản phẩm").setFont(font)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setMarginLeft(10f));
            totalTable2.addCell(new Cell().add(new Paragraph(decimalFormat1.format(totalSum))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginLeft(10f));
            document.add(totalTable2);

            document.add(divider2.setBorder(gb2).setMarginBottom(12f));

            document.add(divider.setBorder(gb3).setMarginBottom(5f));
            document.add(new Paragraph("Lưu ý: Sản phẩm được trả hàng trong vòng 3 ngày kể từ ngày nhận hàng nếu sản phẩm bị lỗi do nhà sản xuất.").setFont(font).setItalic());

            document.close();

            return byteArrayOutputStream.toByteArray();
        }
    }

}

