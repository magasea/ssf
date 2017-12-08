package com.shellshellfish.aaas.assetallocation.tools;


import com.mathworks.toolbox.javabuilder.MWComponentOptions;
import com.mathworks.toolbox.javabuilder.MWCtfClassLoaderSource;
import com.mathworks.toolbox.javabuilder.MWCtfExtractLocation;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.internal.MWMCR;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class YihuiMCRFactory {
    private static final MWComponentOptions sDefaultComponentOptions;
    private YihuiMCRFactory() {
    }

    public static MWMCR newInstance(MWComponentOptions var0) throws MWException, IOException {
        if (null == var0.getCtfSource()) {
            var0 = new MWComponentOptions(var0);
            var0.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }

        return MWMCR.newInstance(var0, YihuiMCRFactory.class, "../../../../../yihui", "yihui_ECB61D438DB8605EF316F85B3049F45D", new int[]{9, 1, 0});
    }

    public static MWMCR newInstance() throws MWException, IOException {
        return newInstance(sDefaultComponentOptions);
    }

    static {
        sDefaultComponentOptions = new MWComponentOptions(new Object[]{MWCtfExtractLocation.EXTRACT_TO_CACHE, new MWCtfClassLoaderSource(YihuiMCRFactory.class)});
    }
}
