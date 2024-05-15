package com.taixingyiji.single.common.utils;

import com.taixingyiji.base.common.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lhc
 * @date 2020.01.16
 */
@Component
public class POIUtil {


    public final static String XLS = "xls";
    public final static String XLSX = "xlsx";

    public static Workbook getWorkBook(String filepath) {
        File file = new File(filepath);
        // 获得文件名
        String fileName = file.getName();
        // 创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            // 获取excel文件的io流
            InputStream is = new FileInputStream(filepath);
            // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(XLS)) {
                // 2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(XLSX)) {
                // 2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

//    public static String getCellValue(Cell cell) {
//        Object cellValue = "";
//        if (cell == null) {
//            return "";
//        }
//        // 把数字当成String来读，避免出现1读成1.0的情况
//        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//            //cell.setCellType(Cell.CELL_TYPE_STRING);
//        }
//        // 判断数据的类型
//        switch (cell.getCellType()) {
//            case Cell.CELL_TYPE_NUMERIC: // 数字
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    //用于转化为日期格式
//                    try {
//                        Date d = cell.getDateCellValue();
//                        cellValue = d;
//                    } catch (Exception e) {
//                        cellValue = null;
//                    }
//                } else {
//                    cell.setCellType(Cell.CELL_TYPE_STRING);
//                    cellValue = String.valueOf(cell.getStringCellValue());
//                }
//                break;
//            case Cell.CELL_TYPE_STRING: // 字符串
//                cellValue = String.valueOf(cell.getStringCellValue());
//                break;
//            case Cell.CELL_TYPE_BOOLEAN: // Boolean
//                cellValue = String.valueOf(cell.getBooleanCellValue());
//                break;
//            case Cell.CELL_TYPE_FORMULA: // 公式
//                cellValue = String.valueOf(cell.getCellFormula());
//                break;
//            case Cell.CELL_TYPE_BLANK: // 空值
//                cellValue = "";
//                break;
//            case Cell.CELL_TYPE_ERROR: // 故障
//                cellValue = "";
//                break;
//            default:
//                cellValue = "";
//                break;
//        }
//
//        return String.valueOf(cellValue);
//    }


    /**
     * 写入表格
     *
     * @param list
     * @param startRow
     * @param sheet
     * @param filePath
     * @param split
     * @param headList
     * @param title
     * @return
     */
    public static Workbook writeList(List<Map<String, Object>> list, int startRow, String sheet, String filePath, String[] split, List<Map<String, Object>> headList, String title) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet(sheet);
        Row fisrtRow = sheet1.createRow(0);
        Map<String, Integer> headMap = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            int finalI = i;
            Optional<Map<String, Object>> optional = headList.stream().filter(map -> map.get("base_label").equals(split[finalI])).findAny();
            if (optional.isPresent()) {
                Cell cell = fisrtRow.createCell(i);
                cell.setCellValue((String) optional.get().get("base_name"));
                headMap.put(split[i], i);
            } else {
                if ("date".equals(split[i]) || "name".equals(split[i])) {
                    Cell cell = fisrtRow.createCell(i);
                    cell.setCellValue(title);
                    headMap.put(split[i], i);
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet1.createRow(i + startRow);
            Map<String, Object> map = list.get(i);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Cell cell = row.createCell(headMap.get(entry.getKey()));
                cell.setCellValue(String.valueOf(entry.getValue()));
            }
        }
        return workbook;
    }

    /**
     * 写入表格
     *
     * @param list
     * @param startRow
     * @param sheet
     * @param filePath
     * @param split
     * @param headList
     * @param title
     * @return
     */
    public static Workbook writeToDayMonth(List<Map<String, Object>> list, int startRow, String sheet, String filePath, String[] split, Map<String, Object> headList, String title) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet(sheet);
        Row fisrtRow = sheet1.createRow(0);
        Map<String, Integer> headMap = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            Cell cell = fisrtRow.createCell(i);
            cell.setCellValue((String) headList.get(split[i]));
            headMap.put(split[i], i);
        }
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet1.createRow(i + startRow);
            Map<String, Object> map = list.get(i);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!StringUtils.isEmpty(headMap.get(entry.getKey()))) {
                    Cell cell = row.createCell(headMap.get(entry.getKey()));
                    cell.setCellValue(String.valueOf(entry.getValue()));
                }
            }
        }
        return workbook;
    }

    /**
     * 无模板导出
     *
     * @param list
     * @param startRow
     * @param sheet
     * @param fileHead
     * @return
     */
    public static Workbook writeListNoFile(List<Map<String, Object>> list, int startRow, String sheet, String[] fileHead) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.getSheet(sheet);
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet1.createRow(i + startRow);
            Map<String, Object> map = list.get(i);
            int j = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Cell cell = row.createCell(j);
                cell.setCellValue(String.valueOf(entry.getValue()));
            }
        }
        return workbook;
    }

    public static void writeToFile(Workbook workbook, String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
            FileOutputStream fs = new FileOutputStream(file);
            workbook.write(fs);
            workbook.close();
            fs.close();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(POIUtil.class.getClassLoader().getResource("a.xlsx").getPath());
    }
}
