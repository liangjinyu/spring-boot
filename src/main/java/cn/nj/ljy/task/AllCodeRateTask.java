
package cn.nj.ljy.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import cn.nj.ljy.util.excel.ExcelDataModel;
import cn.nj.ljy.util.excel.ExcelReader;

public class AllCodeRateTask {

	public static void main(String[] args) {

		File file = new File("/Users/jinyuliang/Desktop/recommend/3100机构交易数据.xls");
		try {
			ExcelDataModel dataModel = ExcelReader.readExcel(file);
			System.out.println(dataModel.getRowCount());
			System.out.println(dataModel.getHeaders());
			List<List<String>> datas = dataModel.getDataList();

			Map<String, Integer> codeCountMap = new HashMap<String, Integer>();
			Set<String> set = new HashSet<String>();
			for (int i = 0; i < datas.size(); i++) {
				List<String> data = datas.get(i);
				if (data.get(0) != null && !"".equals(data.get(0)) && "0".equals(data.get(5))) {
					set.add(data.get(0));
					Integer existCount = codeCountMap.get(data.get(0));
					if (existCount == null) {
						codeCountMap.put(data.get(0), 1);
					} else {
						codeCountMap.put(data.get(0), existCount + 1);
					}
				}
			}
			System.out.println(set.size());

			// 将map.entrySet()转换成list
			List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
					codeCountMap.entrySet());
			// 通过比较器来实现排序
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					// 升序排序
					return o2.getValue().compareTo(o1.getValue());
				}

			});
			double total = datas.size();
			int findCount = 0;
			for (int i = 0; i < datas.size(); i++) {
				List<String> data = datas.get(i);
				if (data.get(0) != null && !"".equals(data.get(0)) && "0".equals(data.get(5))) {
					
					boolean find = false;
					int index = 0;
					for (int j = 0; j < 10; j++) {
						if (list.get(j).getKey().equals(data.get(0))) {
							find = true;
							index = j + 1;
							break;
						}
					}
					if (find) {
						findCount++;
					}
					
					
				}
			}
			System.out.println("findCount = " + findCount + ", notFindCount = "
					+ (total - findCount) + ", 百分比 = " + findCount / total);



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
