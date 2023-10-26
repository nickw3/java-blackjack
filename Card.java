public class Card {
    int index;
    String url;
    int value;
    boolean hidden;

    Card(int index, String url, int value, boolean hidden){
        this.index = index;
        this.url = url;
        this.value = value;
        this.hidden = hidden;
    }

    int getIndex() {
        return this.index;
    }

    String getUrl(){
        return this.url;
    }

    int getValue(){
        return this.value;
    }

    boolean isHidden(){
        return this.hidden;
    }

    void setHidden(boolean bool){
        this.hidden = bool;
        if(bool == true){
            this.url = "image/card/b2fv.png";
        }
        if(bool == false){
            this.url = "image/card/" + this.index + ".png";
        }
    }

}
