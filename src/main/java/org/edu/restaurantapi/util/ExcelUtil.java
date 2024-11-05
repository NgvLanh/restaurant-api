package org.edu.restaurantapi.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.edu.restaurantapi._interface.ExcelExportable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtil {

    public static <T extends ExcelExportable> byte[] generateExcelFile(List<T> successfulRecords, List<T> failedRecords) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Tạo sheet thành công
            Sheet successSheet = workbook.createSheet("Success Records");
            createHeaderRow(successSheet);
            int successRowNum = 1;
            for (T record : successfulRecords) {
                Row row = successSheet.createRow(successRowNum++);
                fillRowWithRecord(row, record);
            }

            // Tạo sheet thất bại
            Sheet failureSheet = workbook.createSheet("Failed Records");
            createHeaderRow(failureSheet);
            int failureRowNum = 1;
            for (T record : failedRecords) {
                Row row = failureSheet.createRow(failureRowNum++);
                fillRowWithRecord(row, record);
            }

            // Xuất file Excel ra byte array
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Field1");
        headerRow.createCell(1).setCellValue("Field2");
        // Thêm các tiêu đề khác nếu cần
    }

    private static <T extends ExcelExportable> void fillRowWithRecord(Row row, T record) {
        row.createCell(0).setCellValue(record.getField1());
        row.createCell(1).setCellValue(record.getField2());
    }
}
