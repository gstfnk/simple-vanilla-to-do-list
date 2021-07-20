package io.github.gstfnk.lang;

class LangDTO {
    private final Integer id;
    private String code;

    LangDTO(Lang lang) {
        this.id = lang.getId();
        this.code = lang.getCode();
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
