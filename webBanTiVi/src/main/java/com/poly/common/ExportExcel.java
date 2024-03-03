package com.poly.common;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExportExcel<T> {
    Workbook workbook;
    Sheet sheet;

    public void exportExel(HttpServletResponse response, List<T> listData, List<Object> listHeader) {
        workbook = new XSSFWorkbook();
        try {
            // Tạo một trang tính mới
            sheet = workbook.createSheet();
            // Tạo dòng và cột và viết dữ liệu vào tệp Excel
            int rowNumber = 1;
            //style cho row
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            creatRow(listHeader, sheet, 0, cellStyle);// tạo tên cột
            for (T temp : listData) {
                List<Object> listCell = new ArrayList<>();
                Class<?> tempClass = temp.getClass();
                Field[] fields = tempClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true); // Cho phép truy cập các thuộc tính private
                    Object value = field.get(temp);
                    listCell.add(value);
                }
                listCell.remove(listCell.size() - 1);
                creatRow(listCell, sheet, rowNumber, cellStyle);// tạo dữ liệu từng dòng
                rowNumber++;
            }

            // Lưu tệp Excel
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void creatRow(List<Object> list, Sheet sheet, Integer rowNumber, CellStyle cellStyle) {
        Row row = sheet.createRow(rowNumber);
        int index = 0;
        for (Object temp : list) {
            Cell cell = row.createCell(index);
            if(temp==null){
                temp="Không lấy được dữ liệu";
            }
            if (temp instanceof Integer) {
                cell.setCellValue((Integer) temp);
            } else if (temp instanceof Long) {
                cell.setCellValue((Long) temp);
                System.out.println((Long) temp);
            } else if (temp instanceof String) {
                cell.setCellValue((String) temp);
            } else if (temp instanceof Float) {
                cell.setCellValue((Float) temp);
            } else if (temp instanceof Date) {
                cell.setCellValue((Date) temp);
            } else {
                cell.setCellValue((Boolean) temp);
            }
            if (rowNumber == 0) {
                cell.setCellStyle(cellStyle);
            }
            sheet.autoSizeColumn(index);
            index++;
        }
    }
}
