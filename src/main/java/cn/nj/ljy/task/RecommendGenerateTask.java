package cn.nj.ljy.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import cn.nj.ljy.util.excel.ExcelDataModel;
import cn.nj.ljy.util.excel.ExcelReader;

public class RecommendGenerateTask {

	public static void main(String[] args) {
		
		File file = new File("/Users/jinyuliang/Desktop/recommend/3100机构交易数据.xls");
		try {
			ExcelDataModel dataModel = ExcelReader.readExcel(file);
			System.out.println(dataModel.getRowCount());
			System.out.println(dataModel.getHeaders());
			List<List<String>> datas = dataModel.getDataList();
//			String testCode = "00106";
//			test(datas, testCode);
			
			for(int i=0;i<datas.size()&&i<100;i++) {
				List<String> data = datas.get(i);
				if(data.get(0)!=null &&!"".equals(data.get(0))&&"0".equals(data.get(5))) {
					test(datas,data.get(0));
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void test(List<List<String>> datas, String testCode) {
		List<String> nextCodeList = new ArrayList<String>();
		Map<String,Integer> testCodeFollowCodeCountMap = new TreeMap<String,Integer> ();
		for(int i=0;i<datas.size();i++) {
			List<String> data = datas.get(i);
			if(testCode.equals(data.get(0)) &&"0".equals(data.get(5))) {
				boolean nexCodeAdd = false;
				int j = 1;
				int count = 1;
				while(i+j<datas.size()) {
					
					List<String> tempData = datas.get(i+j);
					
					if(tempData.get(0)!=null&&!"".equals(tempData.get(0).trim())&&"0".equals(data.get(5))) {
						if(!nexCodeAdd) {
							nextCodeList.add(tempData.get(0));
							nexCodeAdd = true;
						}
						Integer existCount = testCodeFollowCodeCountMap.get(tempData.get(0));
						if(existCount == null ) {
							testCodeFollowCodeCountMap.put(tempData.get(0), 1);
						}else {
							testCodeFollowCodeCountMap.put(tempData.get(0), existCount+1);
						}
						count++;
					}
					j++;
				}
			}
		}
//		System.out.println(testCodeFollowCodeCountMap);
		
		
		
		
		  // 将map.entrySet()转换成list
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(testCodeFollowCodeCountMap.entrySet());
		// 通过比较器来实现排序
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
		    @Override
		    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		        // 升序排序
		        return o2.getValue().compareTo(o1.getValue());
		    }

		});
//		for (Map.Entry<String, Integer> mapping : list) {
//		    System.out.println(mapping.getKey() + ":" + mapping.getValue());
//		}
//		System.out.println(nextCodeList);
		
		int findCount = 0;
		for(String nextCode:nextCodeList) {
			boolean find = false;
			int index = 0;
			for(int i=0;i<20;i++) {
				if(list.get(i).getKey().equals(nextCode)) {
					find = true;
					index = i+1;
					break;
				}
			}
			if(find) {
				findCount++;
			}
//			System.out.println(find?nextCode+" find,index =  "+index:nextCode+" not find");
			
		}
		
		double total = nextCodeList.size();
		System.out.println("testCode = "+testCode+",findCount = "+findCount+", notFindCount = "+(nextCodeList.size()-findCount) +", 百分比 = "+findCount/total);
	}

}
