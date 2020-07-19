package huffcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HuffTree {
	
	public HuffNode root;
	/*
	 * *�����ַ����и��ֳ��ֵĴ���
	 */
	public void culate(String s,Map<Character,Integer> board) {
		for(int i=0;i<s.length();i++) {
			char c = s.charAt(i);
			if(board.containsKey(c)) {		//����map���Ƿ���ڵ�ǰ��ȡ���ַ�
				Integer count = board.get(c);
				count += 1;
				board.put(c,count);
				}else {
				board.put(c, 1);
				}
		}
	}
	/*
	 * *�Թ�ϣ���������
	 */
	public void hashsort(Map<Character,Integer> board,List<Map.Entry<Character,Integer>> list) {
		 list.sort(new Comparator<Map.Entry<Character, Integer>>() {
	          @Override
	          public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
	              return o1.getValue().compareTo(o2.getValue());
	          }
	      });
	}
	/*
	 * *������������
	 */
	public HuffNode create(List<Map.Entry<Character,Integer>> list) {
		ArrayList<HuffNode> nodes = new ArrayList<HuffNode>();
		for(int i =0;i<list.size();i++) {
			nodes.add(new HuffNode(list.get(i).getKey(),list.get(i).getValue()));		//��������������������ӹ�ϣ������ӽڵ�
		}
	//	System.out.println("�����"  +  list);

		while(nodes.size()>1) {
			list.sort(new Comparator<Map.Entry<Character, Integer>>() {
		          @Override
		          public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
		              return o1.getValue().compareTo(o2.getValue());
		          }
		      }); 	//����
			HuffNode l = nodes.get(0);		//ȡ��ǰ������㼴��С���������
			HuffNode r = nodes.get(1);
			r.code = "0";					//�������ĳ�ֵ
			l.code = "1";
			transToCode(r);					//��������
			transToCode(l);
			HuffNode p = new HuffNode(l,r,l.value+r.value,-1);		//��������Ȩֵ����γɵĽ��
			
			nodes.remove(0);	//��ǰ��������Ѽ��������������ɾȥ
			nodes.remove(0);
			
			nodes.add(p);		//�������µ����Ȩֵ�����ӵ�������
		}
		root = nodes.get(0);
		return nodes.get(0);
		
	}
	/*
	 * *ǰ������ -> ��������
	 */
	public void display(HuffNode T) {
		if(T != null) {
			if(T.isPapa != -1)			//isPaPa Ϊ-1�Ķ��Ǻ����γɵĽ�㣬�����б������壬
			System.out.print(T.word + "   "+T.code+"   ");
			display(T.lchild);
			display(T.rchild);
		}
	}
	/*
	 * *�Թ����������б���
	 */
	public void transToCode(HuffNode T) {
		if(T.lchild != null) {			//��������������������0��1ԭ����ӱ���
			T.lchild.code = T.code +"0";
			transToCode(T.lchild );
		}
		if(T.rchild != null) {
			T.rchild.code = T.code + "1";
			transToCode(T.rchild);
		}
	}
	/*
	 **�������ַ������� 
	 */
	String hfmCode = "";	//���������ɺ���ַ���

	public void search(HuffNode T,Character str) {
		if(T.lchild == null && T.rchild == null) {
			if(str.equals(T.word)) {	//�����ֵ�Ƿ�����������е�ֵ��word��ƥ��
				hfmCode += T.code;
			}
		}
		if(T.lchild != null) {			//������������ƥ����Ӧ�ı���
			search(T.lchild,str);
		}
		if(T.rchild != null) {
			search(T.rchild,str);
		}
	}
	public String toHufmCode(String str) {

        for (int i = 0; i < str.length(); i++) {
            Character c = str.charAt(i) ;		//����ƥ��
            search(root, c);
        }

        return hfmCode;					//���ر�����ɺ���ַ���
    }

	/*
	 ** �Թ����������н���
	 */
	String result = "";			//����������ַ�
	boolean target = false;		//��־ƥ���Ƿ�ɹ�
	public void match(HuffNode T,String code) {
		if(T.lchild == null && T.rchild == null) {
			if(code.equals(T.code)) {			//�����ֵ�Ƿ�����������еı���ƥ��
				result += T.word;
				target = true;
			}
		}
		if(T.lchild != null) {		//��������������ƥ����Ӧ��ֵ 
			match(T.lchild,code);
		}
		if(T.rchild != null) {
			match(T.rchild,code);
		}
	}
	public String CodeToWord(String codeStr) {
		int start = 0;
		int end = 1;
		while(end <= codeStr.length()) {
			target = false;
			String s =codeStr.substring(start,end);			//�и�Ȼ��ʼƥ��
			match(root,s);
			if(target) {			//˵��ƥ��ɹ�
				start = end;		//�ı��иʼ��λ�ã���Ϊǰ���Ѿ�ƥ�����	
			}
			end++;
		}
		return result;			//���ؽ�����ɺ���ַ���
	}
	public static void main(String[] args) {
		HuffTree a = new HuffTree();
		Map<Character,Integer> board = new HashMap<Character,Integer>();
		
		System.out.println("�������ַ�����");
		Scanner sc = new Scanner(System.in);
		String qaq = sc.next();
		sc.close();
		a.culate(qaq,board); 			//�����ַ����и��ַ����ֵ�Ƶ��
		List<Map.Entry<Character,Integer>> list = new ArrayList<Map.Entry<Character,Integer>> (board.entrySet());
		a.hashsort(board,list);			///��Ƶ�ʱ��������
		HuffNode root1 = a.create(list);		//������������
		System.out.println("��������£�");
		a.display(root1);					//��������
		System.out.println();
		System.out.println("�����");
		String cc =a.toHufmCode(qaq) ;			//����������ַ�������
		System.out.println(cc);					//�������
		System.out.println("�����");
		System.out.println(a.CodeToWord(cc)+"");	//�������������
	//	System.out.println("   "+board);
	}

}
