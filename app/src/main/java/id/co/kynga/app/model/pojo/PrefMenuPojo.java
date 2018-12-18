package id.co.kynga.app.model.pojo;

/**
 * @author Aji Subastian
 */

@SuppressWarnings("unused")
public class PrefMenuPojo {

    private int imageRes;
    private String menuTitle;

    public PrefMenuPojo() {
        //empty constructor
    }

    public PrefMenuPojo(int imageRes, String menuTitle) {
        this.imageRes = imageRes;
        this.menuTitle = menuTitle;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }
}
