package com.example.prime.Model;

public class AddDataModel {
        String name;
        String type;
        String level;
        String price;
        String pulse;

        public AddDataModel(String name, String type, String level, String price, String pulse) {
            this.name = name;
            this.type = type;
            this.level = level;
            this.price = price;
            this.pulse = pulse;
        }

        public AddDataModel() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPulse() {
            return pulse;
        }

        public void setPulse(String pulse) {
            this.pulse = pulse;
        }

        private boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        private int pos;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

}