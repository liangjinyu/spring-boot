package cn.nj.ljy.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

    public final static int MAX_EXCEL_ROWS = 65535;

//    public static void main(String[] args) throws Exception {
//        List<String> titles = new ArrayList<String>();
//        titles.add("姓名");
//        generateExcelFile(new File("test.xlsx"), titles, null);
//    }

    public static <T> void generateExcel(OutputStream outputStream, List<String> titles, List<T> modelListForExporting)
            throws Exception {
        XSSFWorkbook workbook = generateExcelWorkBook(titles, modelListForExporting);
        try {
            workbook.write(outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static <T> void generateExcelFile(String file, List<String> titles, List<T> modelListForExporting)
            throws Exception {
        generateExcelFile(new File(file), titles, modelListForExporting);
    }

    public static <T> void generateExcelFile(File file, List<String> titles, List<T> modelListForExporting)
            throws Exception {
        File directory = new File(file.getParent());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return;
            }
        }
        XSSFWorkbook workbook = generateExcelWorkBook(titles, modelListForExporting);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static <T> XSSFWorkbook generateExcelWorkBook(List<String> titles, List<T> modelListForExporting)
            throws IllegalArgumentException, IllegalAccessException {
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet();
        XSSFCellStyle headStyle = getHeadStyle(workBook);
        XSSFCellStyle bodyStyle = getBodyStyle(workBook);
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);

        XSSFCell cell = null;

        for (int i = 0; i < titles.size(); i++) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(titles.get(i).toString());
        }

        if (modelListForExporting != null && !modelListForExporting.isEmpty()) {
            T obj = modelListForExporting.get(0);
            Field[] fields = obj.getClass().getDeclaredFields();

            if (modelListForExporting != null && modelListForExporting.size() > 0) {
                for (int i = 0; i < modelListForExporting.size(); i++) {
                    XSSFRow bodyRow = sheet.createRow(i + 1);
                    int j = 0;
                    for (Field field : fields) {
                        // 仅针对公共字段
                        field.setAccessible(true);
                        // 不需要填充serialVersionUID
                        if (field.getName() == "serialVersionUID") {
                            continue;
                        }
                        cell = bodyRow.createCell(j);
                        cell.setCellStyle(bodyStyle);
                        String value = field.get(modelListForExporting.get(i)) != null
                                ? field.get(modelListForExporting.get(i)).toString() : "";
                        cell.setCellValue(value);
                        j++;
                    }
                }
            }

        }

        for (int i = 0; i < titles.size(); i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }

//        sheet = setHSSFValidation(sheet, 0, MAX_EXCEL_ROWS, 11, 11, "15", "最多15个字符");// 最多30个字符,15个中文.
//        sheet = setHSSFValidation(sheet, 0, MAX_EXCEL_ROWS, 12, 12, "5", "最多5个字符");// 最多10个字符,5个中文.
//        sheet = setHSSFValidation(sheet, 0, MAX_EXCEL_ROWS, 13, 13, "25", "最多25个字符");// 最多50个字符,25个中文.
//        sheet = setHSSFValidation(sheet, 0, MAX_EXCEL_ROWS, 15, 15, "500", "最多500个字符");// 最多1000个字符,500个中文.

        return workBook;
    }

    /**
     * 设置表头的单元格样式
     *
     * @return
     */
    public static XSSFCellStyle getHeadStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格的背景颜色为淡蓝色
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 设置表体的单元格样式
     *
     * @return
     */
    public static XSSFCellStyle getBodyStyle(XSSFWorkbook wb) {
        // 创建单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 创建单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = wb.createFont();
        // 设置字体加粗
        // font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        return cellStyle;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     * 
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    public static XSSFSheet setHSSFValidation(XSSFSheet sheet, int firstRow, int endRow, int firstCol, int endCol,
            String maxLength, String msg) {
        XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(
                DVConstraint.ValidationType.TEXT_LENGTH, DVConstraint.OperatorType.BETWEEN, "1", maxLength);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidationHelper help = new XSSFDataValidationHelper(sheet);
        DataValidation validation = help.createValidation(constraint, regions);
        // 数据有效性对象
        validation.createErrorBox("输入值有误", msg);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
        return sheet;
    }

    /**
     * 
     * 功能描述: <br>
     * 对excel数据进行转换操作
     *
     * @version [V1.0, 2017年2月16日]
     * @param hssfCell
     * @return
     */
    public static String parseExcel(XSSFCell hssfCell) {
        String result = new String();
        switch (hssfCell.getCellType()) {
            case XSSFCell.CELL_TYPE_NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (hssfCell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                    Date date = hssfCell.getDateCellValue();
                    result = sdf.format(date);
                } else if (hssfCell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = hssfCell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = hssfCell.getNumericCellValue();
                    XSSFCellStyle style = hssfCell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case XSSFCell.CELL_TYPE_STRING:// String类型
                result = hssfCell.getRichStringCellValue().toString();
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                result = "";
            default:
                result = "";
                break;
        }
        return result;
    }
}
