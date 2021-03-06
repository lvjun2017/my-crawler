package com.xyzj.crawler.spidertask.docrawl.cj;

import com.xyzj.crawler.spidertask.dorule.cj.JiankeSpiderRule;
import com.xyzj.crawler.framework.entity.GoodsPO;
import com.xyzj.crawler.utils.importfrom.ImportExcelUtil;
import com.xyzj.crawler.framework.threads.SpiderTaskMultThread;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JiankeDoMain {
	//0.1 基础准备
	/** 链接头部*/
	private static final String BASE_URL ="https://search.jianke.com/prod?wd=";

	public static void main(String[] args) throws Exception {

		//	1-1 从Excel表中读取目标数据
		File file = new File("D:\\java\\workspace\\learn\\crawler\\record\\cj\\进口药品2018年1月30日.xlsx");
		FileInputStream fileInputStream = new FileInputStream(file);

		//	1-2 将Excel表中的记录保存在map对象中
		Map<String, String> excel2Map = new HashMap<String, String>();
		excel2Map.put("编号", "orderNum");
		excel2Map.put("名称", "name");
		List<Map<String, Object>> excel2List = ImportExcelUtil.parseExcel(fileInputStream, file.getName(), excel2Map);

		// 1-3 将excel2List中的map封装到对象中
		List<GoodsPO> goodsPOList = new ArrayList<>();
		for (int i = 0; i < excel2List.size(); i++) {
			GoodsPO goodsPO = new GoodsPO();
			goodsPO.setOrderNum(excel2List.get(i).get("orderNum").toString());
			goodsPO.setName(excel2List.get(i).get("name").toString());
			goodsPO.setWebUrl(BASE_URL+ URLEncoder.encode(excel2List.get(i).get("name").toString()));
			goodsPOList.add(goodsPO);
		}

		//2-1启动线程进行数据爬取
		ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10);

		for (final GoodsPO goodsPO : goodsPOList) {
			JiankeSpiderRule jiankeSpiderRule = new JiankeSpiderRule();
			cachedThreadPool.execute(new SpiderTaskMultThread(goodsPO, jiankeSpiderRule));
		}
		cachedThreadPool.shutdown();


	}

}
