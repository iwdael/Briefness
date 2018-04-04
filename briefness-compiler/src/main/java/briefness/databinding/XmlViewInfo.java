package briefness.databinding;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
public class XmlViewInfo {
    public String ID;
    public String click;
    public String longClick;
    public String touch;
    public String view;
    public String bind;
    public String clazz;

    @Override
    public String toString() {
        return "XmlViewInfo{" +
                "ID='" + ID + '\'' +
                ", click='" + click + '\'' +
                ", longClick='" + longClick + '\'' +
                ", action='" + touch + '\'' +
                ", view='" + view + '\'' +
                ", bind='" + bind + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }
}
