import java.util.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
public class Sample1 {
  public static void main(String[] args) throws Exception{ 
    SAXBuilder sb=new SAXBuilder();
    
    Document doc=sb.build(Sample1.class.getClassLoader()
    		.getResourceAsStream("beans.xml")); //构造文档对象
    Element root=doc.getRootElement(); //获取根元素HD
    List list=root.getChildren("bean");//取名字为disk的所有元素
    for(int i=0;i<list.size();i++){
       Element element=(Element)list.get(i);
       String id=element.getAttributeValue("id");
       String clazz=element.getAttributeValue("class");
       System.out.println(id);
       System.out.println(clazz);
    }  
  }
} 