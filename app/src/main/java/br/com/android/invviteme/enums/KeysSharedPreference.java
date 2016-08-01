package br.com.android.invviteme.enums;

public enum KeysSharedPreference {

        NAMEUSER(1, "nameUser"),
        EMAILUSER(2, "emailUser");

        private Integer id;
        private String key;

        KeysSharedPreference(Integer id, String key) {
            this.id = id;
            this.key = key;
        }

        public Integer getId() {
            return id;
        }

        public String getKey() {
            return key;
        }

}
