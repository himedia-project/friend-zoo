package com.friendzoo.api.util.excel;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ExcelDataExtractor<T> {

    public List<T> extract(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();

        // 제목줄인 첫 행은 건너 뛴다.
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        StringBuilder exceptionMessage = new StringBuilder();
        int index = 1;

        List<T> list = new ArrayList<>();
        while (rowIterator.hasNext()) {
            try {
                index++;
                Row row = rowIterator.next();
                list.add(map(row));
            } catch (IllegalStateException e) {
                exceptionMessage.append(index).append("행: ").append("값의 타입이 맞지않습니다.\n");
            } catch (NullPointerException e) {
                exceptionMessage.append(index).append("행: ").append("셀에 값이 존재하지 않습니다.\n");
            } catch (IllegalArgumentException e) {
                exceptionMessage.append(index).append("행: ").append(e.getMessage()).append("\n");
            }
        }

        if (!exceptionMessage.isEmpty()) {
            throw new IllegalArgumentException(exceptionMessage.toString());
        }

        return list;
    }

    abstract protected T map(Row row);

}