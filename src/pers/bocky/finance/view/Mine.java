package pers.bocky.finance.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class Mine {

	public static void main(String[] args) throws IOException {
//		Arrays.asList("456", "123").stream().filter(d -> d.contains("5")).forEach(Mine::prin);
//
//		// GBK编码格式源码路径
//		String srcDirPath = "E:\\finance";
//		// 转为UTF-8编码格式源码路径
//		String utf8DirPath = "E:\\utf8\\finance";
//		// 获取所有java文件
//		Collection<File> javaGbkFileCol = FileUtils.listFiles(new File(srcDirPath), new String[] { "java" }, true);
//
//		for (File javaGbkFile : javaGbkFileCol) {
//			// UTF8格式文件路径
//			String utf8FilePath = utf8DirPath + javaGbkFile.getAbsolutePath().substring(srcDirPath.length());
//			// 使用GBK读取数据，然后用UTF-8写入数据
////			FileUtils.writeLines(new File(utf8FilePath), "UTF-8", FileUtils.readLines(javaGbkFile, "GBK"));
//		}

		List<Bean> list1 = new ArrayList<Bean>();
		list1.add(new Bean(1, null, null, "Fund-AA", null));
		list1.add(new Bean(2, null, null, "Fund-B", "Fund-AA"));
		list1.add(new Bean(3, null, null, "Fund-C", "Fund-AA"));
		list1.add(new Bean(4, null, null, "Fund-D", "Fund-AA"));
		list1.add(new Bean(5, null, null, "Fund-EE", null));
		list1.add(new Bean(6, null, null, "Fund-F", "Fund-EE"));
		list1.add(new Bean(7, null, null, "Fund-G", "Fund-EE"));
		list1.add(new Bean(8, null, null, "Fund-HH", null));
		list1.add(new Bean(9, null, null, "Fund-I", "Fund-HH"));
		list1.add(new Bean(10, null, null, "Fund-J", "Fund-HH"));
		list1.add(new Bean(11, null, null, "Fund-KK", null));
		list1.add(new Bean(10, null, null, "Fund-L", "Fund-KK"));
		
		Map<String, Bean> labelMap = new HashMap<String, Bean>();
		labelMap.put("Fund-AA", new Bean(1, 4, "LABEL-1", "Fund-AA", null));
		labelMap.put("Fund-EE", new Bean(2, 2, "LABEL-2", "Fund-EE", null));
		labelMap.put("Fund-HH", new Bean(3, 3, "LABEL-3", "Fund-HH", null));
		labelMap.put("Fund-KK", new Bean(4, 1, "LABEL-4", "Fund-KK", null));
		labelMap.put("Fund-B", new Bean(5, 4, "LABEL-10", "Fund-B", null));
		
		Integer fromIndex = 0;
		Map<Integer, Integer> indicators = new HashMap<Integer, Integer>();
		for(int i = 1; i < list1.size(); i++) {
			if (list1.get(i).getParent() == null) {
				indicators.put(fromIndex, i);
				fromIndex = i;
			}
		}
		indicators.put(fromIndex, list1.size());
		
		Map<Integer, List<Bean>> splitResult = new HashMap<Integer, List<Bean>>();
		List<Bean> tempList;
		Set<Entry<Integer, Integer>> entrySet = indicators.entrySet();
		Iterator<Entry<Integer, Integer>> iterator = entrySet.iterator();
		while(iterator.hasNext()) {
			Entry<Integer, Integer> entry = iterator.next();
			Bean topMostBean = list1.get(entry.getKey());
			Bean labelBean = labelMap.get(topMostBean.getEntityId());
			if (labelBean == null) {
				labelBean = new Bean(0, 9999, "Unlabeled", null, null);
			}
			topMostBean.setParent(labelBean.getName());
			tempList = new ArrayList<Bean>();
			tempList.add(labelBean);
			tempList.addAll(list1.subList(entry.getKey(), entry.getValue()));
			splitResult.put(labelBean.getOrder(), tempList);
		}
		
		List<Map.Entry<Integer, List<Bean>>> splitResultList = new ArrayList<Map.Entry<Integer, List<Bean>>>(splitResult.entrySet());
		Collections.sort(splitResultList, (o1, o2) -> {
			return o1.getKey() - o2.getKey();
		});
		
		Iterator<Entry<Integer, List<Bean>>> itt = splitResultList.iterator();
		List<Bean> outputList = new ArrayList<Bean>();
		while(itt.hasNext()) {
			outputList.addAll(splitResult.get(itt.next().getKey()));
		}
		
		for (Bean bean : outputList) {
			System.out.println(bean);
		}
		
		
		
//		
//		ListIterator<Bean> it = list1.listIterator();
//		
//		while(it.hasNext()) {
//			Bean curBean = it.next();
//			String parent = curBean.getParent();
//			if (parent == null) {
//				String fund = curBean.getEntityId();
//				Bean labelBean = labelMap.get(fund);
//				curBean.setParent(labelBean.getName());
//				curBean.setOrder(labelBean.getOrder());
//				labelBean.setEntityId(null);
//				it.add(labelBean);
//			}
//		}
//		
//		list1.stream().sorted(
//				(d1, d2) -> {
//					System.out.println(d1);
//					return d1.getOrder() - d2.getOrder();
//				}
//		).collect(Collectors.toList());
//		
//		for (Bean bean : list1) {
//			System.out.println(bean);
//		}
	}

	private static void prin(String s) {
		System.out.println("Hello!!! >>> " + s);
	}
}

class Bean {
	private Integer id;
	private Integer order;
	private String name;
	private String entityId;
	private String parent;
	
	
	public Bean(Integer id, Integer order, String name, String entityId, String parent) {
		super();
		this.id = id;
		this.order = order;
		this.name = name;
		this.entityId = entityId;
		this.parent = parent;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "Bean [id=" + id + ", order=" + order + ", name=" + name + ", entityId=" + entityId + ", parent="
				+ parent + "]";
	}
	
	
}
