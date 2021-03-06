/*
 * Copyright 2017 Young Digital Planet S.A.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

var renderer = (function () {
    var navU = navigator.userAgent;

    // Android Mobile
    var isAndroidMobile = navU.indexOf('Android') > -1 && navU.indexOf('Mozilla/5.0') > -1 && navU.indexOf('AppleWebKit') > -1;

    // Apple webkit
    var regExAppleWebKit = new RegExp(/AppleWebKit\/([\d.]+)/);
    var resultAppleWebKitRegEx = regExAppleWebKit.exec(navU);
    var appleWebKitVersion = (resultAppleWebKitRegEx === null ? null : parseFloat(regExAppleWebKit.exec(navU)[1]));

    // Chrome
    var regExChrome = new RegExp(/Chrome\/([\d.]+)/);
    var resultChromeRegEx = regExChrome.exec(navU);
    var chromeVersion = (resultChromeRegEx === null ? null : parseFloat(regExChrome.exec(navU)[1]));

    // Native Android Browser
    var isAndroidStackBrowser = isAndroidMobile && (appleWebKitVersion !== null && appleWebKitVersion < 537) || (chromeVersion !== null && chromeVersion < 37);
    return isAndroidStackBrowser ? "SVG" : "HTML-CSS";
})();
MathJax.Hub.yElements = [];

MathJax.Hub.yProcessElements = function (callback) {
    var elements = MathJax.Hub.yElements;
    if (elements.length === 0) {
        if (typeof(callback) === 'function') {
            callback();
        }
        return;
    }
    if (elements.length === 1) {
        elements = elements[0];
    }

    MathJax.Hub.Queue(["Process", MathJax.Hub, elements, callback]);
    MathJax.Hub.yElements = [];
};

MathJax.Hub.yRerenderElement = function (divId) {
    var jaxElement = MathJax.Hub.getAllJax(divId)[0];
    MathJax.Hub.Queue(["Rerender", jaxElement]);
};

MathJax.Hub.Config({
    extensions: ["Gap.js"],
    jax: ["input/MathML", "output/" + renderer],
    showMathMenu: false,
    showMathMenuMSIE: false,
    showProcessingMessages: false,
    messageStyle: "none",
    "HTML-CSS": {
        availableFonts: ["TeX"],
        preferredFont: "TeX",
        webFont: "TeX",
        imageFont: null,
        matchFontHeight: true,
        mtextFontInherit: true
    },
    SVG: {
        font: "TeX",
        mtextFontInherit: true
    }
});

(function () {
    var HUB = MathJax.Hub;

    HUB.Queue(["setRenderer", HUB, renderer, "jax/mml"]);
})();

MathJax.Ajax.loadComplete("[MathJax]/config/yJax.js");
