package com.ar.ase.common;

import java.io.Serializable;

/**
 * TextObj
 *
 * @author yuanjin
 * @date 2019/12/24
 */
public class TextObj implements Serializable {
    private static final long serialVersionUID = -893678758554183699L;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
