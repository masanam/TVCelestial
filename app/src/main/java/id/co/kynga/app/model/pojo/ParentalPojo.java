package id.co.kynga.app.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aji Subastian
 */

@SuppressWarnings("unused")
public class ParentalPojo implements Parcelable {

    private boolean su;
    private boolean r;
    private boolean bo;
    private boolean d;

    public ParentalPojo() {
        //empty constructor
    }

    public ParentalPojo(boolean su, boolean r, boolean bo, boolean d) {
        this.su = su;
        this.r = r;
        this.bo = bo;
        this.d = d;
    }

    public boolean isSu() {
        return su;
    }

    public void setSu(boolean su) {
        this.su = su;
    }

    public boolean isR() {
        return r;
    }

    public void setR(boolean r) {
        this.r = r;
    }

    public boolean isBo() {
        return bo;
    }

    public void setBo(boolean bo) {
        this.bo = bo;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    private ParentalPojo(Parcel in) {
        su = in.readByte() != 0;
        r = in.readByte() != 0;
        bo = in.readByte() != 0;
        d = in.readByte() != 0;
    }

    public static final Creator<ParentalPojo> CREATOR = new Creator<ParentalPojo>() {
        @Override
        public ParentalPojo createFromParcel(Parcel in) {
            return new ParentalPojo(in);
        }

        @Override
        public ParentalPojo[] newArray(int size) {
            return new ParentalPojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (su ? 1 : 0));
        dest.writeByte((byte) (r ? 1 : 0));
        dest.writeByte((byte) (bo ? 1 : 0));
        dest.writeByte((byte) (d ? 1 : 0));
    }

}
