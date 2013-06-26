package ipx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map.Entry;
import java.util.TreeMap;

public class IPMap {

	private TreeMap<Long, IPRange> mapping;

	public IPMap(File file) throws Exception {
		this.mapping = generateFromFile(file);
		createLocal();
	}

	private void createLocal() throws Exception {
		IPRange classA = new IPRange("10.0.0.0", "10.255.255.255", "LOCAL");
		IPRange classB = new IPRange("172.16.0.0", "172.31.255.255", "LOCAL");
		IPRange classC = new IPRange("192.168.0.0", "192.168.255.255", "LOCAL");
		IPRange localloopback = new IPRange("127.0.0.1", "127.255.255.254", "LOCAL");
		mapping.put(new Long(classA.getStart().toLong()), classA);
		mapping.put(new Long(classB.getStart().toLong()), classB);
		mapping.put(new Long(classC.getStart().toLong()), classC);
		mapping.put(new Long(localloopback.getStart().toLong()), localloopback);
	}

	public IPRange find(IP ip) {
		if (ip == null) {
			return null;
		}

		Entry<Long, IPRange> entry = mapping.floorEntry(ip.toLong());
		if (entry == null) {
			return null;
		}

		IPRange range = entry.getValue();
		if (!(ip.toLong() < range.getEnd().toLong())) {
			return null;
		}

		return range;
	}

	public int size() {
		return this.mapping.size();
	}

	/*-- private --*/

	private TreeMap<Long, IPRange> generateFromFile(File file) throws Exception {
		TreeMap<Long, IPRange> mapping = new TreeMap<Long, IPRange>();

		BufferedReader br = new BufferedReader(new FileReader(file));

		try {
			String line;
			while ((line = br.readLine()) != null) {
				String[] strs = line.split(",");
				IPRange range;
				if (strs.length == 4) {
					range = new IPRange(strs[0], strs[1], strs[2], strs[3]);
				} else {
					range = new IPRange(strs[0], strs[1], strs[2]);
				}

				mapping.put(new Long(range.getStart().toLong()), range);
			}
		} finally {
			br.close();
		}

		return mapping;
	}
}