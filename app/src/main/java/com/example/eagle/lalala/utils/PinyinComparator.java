package com.example.eagle.lalala.utils;



import com.example.eagle.lalala.bean.User;

import java.util.Comparator;

public class PinyinComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// 按照名字排序
		User user0 = (User) arg0;
		User user1 = (User) arg1;
		String catalog0 = "";
		String catalog1 = "";

		if (user0 != null && user0.getName() != null
				&& user0.getName().length() > 1)
			catalog0 = PingYinUtil.converterToFirstSpell(user0.getName())
					.substring(0, 1);

		if (user1 != null && user1.getName() != null
				&& user1.getName().length() > 1)
			catalog1 = PingYinUtil.converterToFirstSpell(user1.getName())
					.substring(0, 1);
		int flag = catalog0.compareTo(catalog1);
		return flag;

	}

}
