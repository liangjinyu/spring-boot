package cn.nj.ljy.util.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import cn.nj.ljy.util.common.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private static final int EXCEL_HEADER_INDEX = 0;
    private static final int EXCEL_HEADER_NUM = 1;
    private static final String EXCEL_2007_SUFFIX = ".xlsx";
    private static final String EXCEL_2003_SUFFIX = ".xls";

    /**
     * 得到表头单个单元格数据
     *
     * @param cell
     * @return
     */
    public String getTitleValue(HSSFCell cell) {
        String strCell = cell.getStringCellValue();
        return strCell;
    }

    /**
     * 读取Excel表格
     * 
     * @param multipartFile
     * @return
     * @throws IOException
     */

    public static ExcelDataModel readExcel(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        fileName = fileName.toLowerCase().trim();
        if (fileName.endsWith(EXCEL_2007_SUFFIX)) {
            return readXlsx(multipartFile.getInputStream());
        } else if (fileName.endsWith(EXCEL_2003_SUFFIX)) {
            return readXls(multipartFile.getInputStream());
        } else {
            System.out.println(" Error excel file ");
            return null;
        }
    }

    /**
     * 读取Excel表格
     */

    public static ExcelDataModel readExcel(File file) throws IOException {
        String fileName = file.getName();
        fileName = fileName.toLowerCase().trim();
        if (fileName.endsWith(EXCEL_2007_SUFFIX)) {
            return readXlsx(new FileInputStream(file));
        } else if (fileName.endsWith(EXCEL_2003_SUFFIX)) {
            return readXls(new FileInputStream(file));
        } else {
            System.out.println(" Error excel file ");
            return null;
        }
    }

    /**
     * 读取Excel表格
     * 
     * @param multipartFile
     * @return
     * @throws IOException
     */

    public static List<ExcelDataModel> readExcelModels(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        fileName = fileName.toLowerCase().trim();
        if (fileName.endsWith(EXCEL_2007_SUFFIX)) {
            return readXlsxModels(multipartFile.getInputStream());
        } else {
            System.out.println(" Error excel file ");
            return null;
        }
    }

    /**
     * 2003解析
     * 
     * @param multipartFile
     * @return
     * @throws IOException
     */
    private static ExcelDataModel readXls(InputStream inputStream) throws IOException {
        ExcelDataModel excelDataModel = new ExcelDataModel();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);

        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        HSSFRow row = sheet.getRow(0);
        int rowCount = sheet.getPhysicalNumberOfRows() - EXCEL_HEADER_NUM;
        excelDataModel.setRowCount(rowCount);
        int columnCount = row.getPhysicalNumberOfCells();
        excelDataModel.setColumnCount(columnCount);
        List<String> headers = getRowValues(sheet, EXCEL_HEADER_INDEX, columnCount);
        excelDataModel.setHeaders(headers);

        List<List<String>> dataList = new ArrayList<List<String>>();
        int lastRow = sheet.getLastRowNum();
        for (int i = EXCEL_HEADER_NUM; i <= lastRow; i++) {
            List<String> rowDatas = getRowValues(sheet, i, columnCount);
            if (rowDatas != null && !rowDatas.isEmpty()) {
                dataList.add(rowDatas);
            } else {
                dataList.add(getNullStringList(columnCount));
            }
        }
        removeLastNullStringList(dataList);
        excelDataModel.setDataList(dataList);
        return excelDataModel;
    }

    private static void removeLastNullStringList(List<List<String>> dataList) {

        int size = dataList.size() - 1;
        for (int i = size; i >= 0; i--) {
            List<String> tempRow = dataList.get(i);
            if (isNullStringList(tempRow)) {
                dataList.remove(i);
            } else {
                break;
            }
        }
    }

    private static boolean isNullStringList(List<String> tempRow) {
        boolean isNullStringList = true;
        if (tempRow != null && !tempRow.isEmpty()) {
            for (String value : tempRow) {
                if (!value.trim().equals("")) {
                    isNullStringList = false;
                    break;
                }
            }
        }
        return isNullStringList;
    }

    private static List<String> getNullStringList(int columnCount) {
        List<String> list = new ArrayList<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            list.add("");
        }
        return list;
    }

    private static List<String> getRowValues(HSSFSheet sheet, int rowIndex, int headerColumnCount) {
        List<String> values = new ArrayList<>();
        HSSFRow row = sheet.getRow(rowIndex);

        if (row != null) {
            int cellNum = row.getLastCellNum();
            int maxValidColumn = Math.min(cellNum, headerColumnCount);
            for (int i = 0; i < maxValidColumn; i++) {
                String cellValue = getCellValue(row.getCell(i));
                values.add(cellValue);
            }
            int nullNum = headerColumnCount - cellNum;
            for (int i = 0; i < nullNum; i++) {
                values.add("");
            }
        }
        return values;
    }

    private static List<String> getRowValues(XSSFSheet sheet, int rowIndex, int headerColumnCount) {
        List<String> values = new ArrayList<>();
        XSSFRow row = sheet.getRow(rowIndex);

        if (row != null) {
            int cellNum = row.getLastCellNum();
            int maxValidColumn = Math.min(cellNum, headerColumnCount);
            for (int i = 0; i < maxValidColumn; i++) {
                String cellValue = getCellValue(row.getCell(i));
                values.add(cellValue);
            }
            int nullNum = headerColumnCount - cellNum;
            for (int i = 0; i < nullNum; i++) {
                values.add("");
            }
        }
        return values;
    }

    /**
     * 2007解析
     * 
     * @param multipartFile
     * @return
     * @throws IOException
     */
    private static ExcelDataModel readXlsx(InputStream inputStream) throws IOException {
        ExcelDataModel excelDataModel = new ExcelDataModel();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);
        int rowCount = sheet.getPhysicalNumberOfRows();
        excelDataModel.setRowCount(rowCount);
        int columnCount = row.getPhysicalNumberOfCells();
        excelDataModel.setColumnCount(columnCount);
        List<String> headers = getRowValues(sheet, EXCEL_HEADER_INDEX, columnCount);
        excelDataModel.setHeaders(headers);

        List<List<String>> dataList = new ArrayList<List<String>>();
        for (int i = EXCEL_HEADER_NUM; i < rowCount; i++) {
            List<String> rowDatas = getRowValues(sheet, i, columnCount);
            if (rowDatas != null && !rowDatas.isEmpty()) {
                dataList.add(rowDatas);
            }
        }
        excelDataModel.setDataList(dataList);
        return excelDataModel;
    }

    private static List<ExcelDataModel> readXlsxModels(InputStream inputStream) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        int sheetNum = xssfWorkbook.getNumberOfSheets();
        List<ExcelDataModel> list = new ArrayList<ExcelDataModel>();
        for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {

            ExcelDataModel excelDataModel = new ExcelDataModel();
            XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetIndex);
            excelDataModel.setSheetName(sheet.getSheetName());
            XSSFRow row = sheet.getRow(0);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int columnCount = row.getPhysicalNumberOfCells();
            excelDataModel.setColumnCount(columnCount);
            List<String> headers = getRowValues(sheet, EXCEL_HEADER_INDEX, columnCount);
            excelDataModel.setHeaders(headers);

            List<List<String>> dataList = new ArrayList<List<String>>();
            for (int i = EXCEL_HEADER_NUM; i < rowCount; i++) {
                List<String> rowDatas = getRowValues(sheet, i, columnCount);
                if (rowDatas != null && !rowDatas.isEmpty() && isAllEmpty(rowDatas)) {
                    dataList.add(rowDatas);
                }
            }
            excelDataModel.setDataList(dataList);
            excelDataModel.setRowCount(dataList.size());
            list.add(excelDataModel);
        }
        return list;
    }
    
    private static boolean isAllEmpty(List<String> rowDatas){
        for (String rowData : rowDatas) {
            if(StringUtil.isNotBlank(rowData)){
                return true;
            }
        }
        return false;
    }

    /**
     * 得到表单元格数据
     *
     * @param cell
     * @return
     */
    private static String getCellValue(XSSFCell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case XSSFCell.CELL_TYPE_NUMERIC:
                    DecimalFormat df = new DecimalFormat("00000000.00");
                    cellValue = df.format(cell.getNumericCellValue());
                    break;
                case XSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case XSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case XSSFCell.CELL_TYPE_FORMULA:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case XSSFCell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                case XSSFCell.CELL_TYPE_ERROR:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString().trim();
                    break;
            }
        }
        return cellValue.trim();
    }

    private static String getCellValue(HSSFCell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    DecimalFormat df = new DecimalFormat("0");
                    cellValue = df.format(cell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString().trim();
                    break;
            }
        }
        return cellValue.trim();
    }

}
