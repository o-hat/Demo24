import java.util.ArrayList;
import java.util.Random;


public class GameTen {

    public static boolean loop = true; // 只计算一个答案

    public static void doTest(ArrayList<Object> arr) throws Exception {
        if (arr.size() > 1) {
            for (int i = 0; i < arr.size() - 1; i++) {
                int j = i + 1;
                while (loop && j < arr.size()) {
                    ArrayList<Object> copyData = (ArrayList<Object>) arr.clone();
                    copyData.remove(arr.get(i));
                    copyData.remove(arr.get(j));
//                    System.out.println("a=" + arr.get(i) + "   b=" + arr.get(j) + "  剩下：" + copyData.toString());
                    copyData.add(returnTwoStr(arr.get(i), arr.get(j)));
//                    System.out.println(copyData);
                    doTest(copyData);
                    j += 1;
                }
            }
        } else {
//            System.out.println("最终结果:" + arr.toString());
            // 递归去算每一个式子
            dgArrayList(arr);
        }

    }

    private static void dgArrayList(ArrayList<Object> arr) throws Exception {
        int i = 0;
        while (loop && i < arr.size()) {
            Object o = arr.get(i);
            if (o instanceof ArrayList) {
                dgArrayList((ArrayList<Object>) o);
            } else if (o instanceof String) {
                double total = StringToBigDecimal.doMath((String) o);
                if (total == 24) {
                    System.out.println(o + " =24");
                    loop = false;
                }
            } else {
                System.out.println("最后递归计算错误，子节点类型错误：" + o);
            }
            i += 1;
        }
    }


    public static ArrayList<Object> returnTwoStr(Object a, Object b) throws Exception {
        ArrayList<Object> arr = new ArrayList<Object>();
        if (a instanceof ArrayList && !(b instanceof ArrayList)) {
            for (int i = 0; i < ((ArrayList) a).size(); i++) {
                arr.add(returnTwoStr(((ArrayList) a).get(i), b));
            }
        } else if (b instanceof ArrayList && !(a instanceof ArrayList)) {
            for (int i = 0; i < ((ArrayList) b).size(); i++) {
                arr.add(returnTwoStr(((ArrayList) b).get(i), a));
            }
        } else if (a instanceof ArrayList && b instanceof ArrayList) {
            for (int i = 0; i < ((ArrayList) a).size(); i++) {
                for (int j = 0; j < ((ArrayList) b).size(); j++) {
                    arr.add(returnTwoStr(((ArrayList) a).get(i), ((ArrayList) b).get(j)));
                }
            }
        } else {
            arr.add("(" + a + "+" + b + ")");
            arr.add("(" + a + "-" + b + ")");
            arr.add("(" + b + "-" + a + ")");
            arr.add(a + "*" + b);
            if (b instanceof Integer && ((Integer) b).intValue() != 0) { // 如果是数字 去掉0的选项
                arr.add(a + "/" + b);
            } else if (b instanceof String && StringToBigDecimal.doMath((String) b) != 0) { // 如果是字符串 去掉结果是0的选项
                arr.add(a + "/" + b);
            }
            if (a instanceof Integer && ((Integer) a).intValue() != 0) { // 如果是数字 去掉0的选项
                arr.add(b + "/" + a);

            } else if (a instanceof String && StringToBigDecimal.doMath((String) a) != 0) { // 如果是字符串 去掉结果是0的选项
                arr.add(b + "/" + a);
            }
        }

        return arr;
    }


    public static void main(String[] args) throws Exception {
        ArrayList<Object> arr = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            arr.add(new Random().nextInt(15));
        }
        System.out.println(arr.toString());
        long startTime = System.currentTimeMillis();
        doTest(arr);
        long endTime = System.currentTimeMillis();
        System.out.println("运行耗时：" + (endTime - startTime) + "ms");
    }
}
