import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


/**
 * 中文"("和"（"是不一样的 中英文括号
 * "s" 和 's' 也是不一样的 一个是String 一个是char
 */
public class StringToBigDecimal {

//    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("[0-9\\.+-/*()= ]+");

    private static final ArrayList<Character> opts = new ArrayList<Character>() {{
        add('+');
        add('-');
        add('*');
        add('/');
    }};

    /**
     * 通过后缀表达式计算值
     *
     * @param list
     * @return
     */
    public static double calStringValue(ArrayList<String> list) throws Exception {
        Stack<Double> values = new Stack<>(); // 值的栈
        for (String str : list) {
            if (Character.isDigit(str.charAt(0))) { // 如果是数值
                values.push(Double.parseDouble(str));
            } else {
                double res = 0.0;
                double a = values.pop();
                double b = values.pop();
                if (str.equals("+")) {
                    res = b + a;
                } else if (str.equals("-")) {
                    res = b - a;
                } else if (str.equals("*")) {
                    res = b * a;
                } else if (str.equals("/")) {
                    if (a != 0) {
                        res = b / a;
                    }
                } else {
                    System.out.println("计算符号错误：" + str);
//                    throw new Exception("错误");
                }
                values.push(res);
            }
        }
        return values.pop();
    }

    /**
     * 比较运算符的优先级
     * 返回 左边是不是一定比右边大
     * !!!优先级的问题 '+' 号碰到了 '(' 该返回什么。。。
     *
     * @return
     */
    public static boolean compareOpts(char left, char right) {
        if (left == '*' || left == '/') {
            if (right == '*' || right == '/') {
                return false;
            }
            return true;
        }
        if (left == '+' || left == '-') {
            return right == '(';
        }
        return false;
    }

    /**
     * 转成list
     *
     * @param str
     * @return
     */
    public static ArrayList<String> getStringList(String str) {
//        String trim = str.trim();          // 妈的 这个是去首尾的空格 我说怎么一直错了
        String trim = str.replace(" ", "");
        ArrayList<String> res = new ArrayList<>();
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < trim.length(); i++) {
            char c = trim.charAt(i);
            if (Character.isDigit(c)) {
                num.append(c);
            } else {
                if (!num.toString().equals("")) {
                    res.add(num.toString());
                }
                res.add(c + "");
                num = new StringBuilder();
            }

        }
        if (!num.toString().equals("")) {
            res.add(num.toString());
        }
        return res;
    }

    /**
     * 中缀表达式 === > 后缀表达式
     * <p>
     * 0. 创建一个操作符的栈
     * 1. 如果是数字 直接进队列
     * 2. 如果是括号
     * I: 如果是 '（' 入栈
     * II: 如果是 ')' 操作符的栈直接出栈 直到碰到 ')'
     * 3. 如果是操作符
     * I: 如果操作符的优先级比top的优先级高，入栈
     * II: 否则top出栈插入到后面，重复3，然后入栈
     * 4. 如果数字都读完了，依次出栈补到后面。
     */
    public static ArrayList<String> middleToBack(ArrayList<String> list) throws Exception {
        Stack<String> stack = new Stack<>();
        ArrayList<String> res = new ArrayList<String>();
        for (String c : list) {
            if (Character.isDigit(c.charAt(0))) {
                res.add(c);
            } else if ("(".equals(c)) {
                stack.push(c);
            } else if (")".equals(c)) {
                while (!stack.isEmpty() && !"(".equals(stack.peek())) {
                    res.add(stack.pop());
                }
                stack.pop(); // 弹出右括号
            } else if (opts.contains(c.charAt(0))) {
                if (stack.isEmpty() || compareOpts(c.charAt(0), stack.peek().charAt(0))) {
                    stack.push(c);
                } else {
                    while (!stack.isEmpty() && !compareOpts(c.charAt(0), stack.peek().charAt(0))) {
                        res.add(stack.peek());
                        stack.pop();
                    }
                    stack.push(c);
                }
            } else {
                System.out.println("中缀表达式 === > 后缀表达式  错误:" + c);
            }
        }
        while (!stack.isEmpty()) {
            res.add(stack.pop());
        }
        return res;
    }

    /**
     * 调用这个计算
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static double doMath(String str) throws Exception {
        ArrayList<String> stringList = getStringList(str);
//        System.out.println("字符串list：" + stringList.toString());
        ArrayList<String> back = middleToBack(stringList);
//        System.out.println("后缀表达式的值为：" + back.toString());
        return calStringValue(back);
    }

    public static void main(String[] args) throws Exception {
//        String s = "12 * (3 + 4) - 6 + 8 / 2";
        String s = "((5+5)+12/14)";
//        String s = "8/5/(8+7)";
//        ArrayList<String> stringList = getStringList(s);
//        System.out.println(stringList);
//        ArrayList<String> back = middleToBack(stringList);
//        ArrayList<String> demo = new ArrayList<String>(Arrays.asList("1", "11", "+", "7", "+", "14", "+"));
//        double a = calStringValue(demo);
//        System.out.println(a);
        double a = doMath(s);
        System.out.println(a);
    }
}
