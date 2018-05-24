;(function(window) {

var svgSprite = '<svg>' +
  ''+
    '<symbol id="icon-account" viewBox="0 0 1024 1024">'+
      ''+
      '<path d="M512 170.666667C606.293333 170.666667 682.666667 247.04 682.666667 341.333333 682.666667 435.626667 606.293333 512 512 512 417.706667 512 341.333333 435.626667 341.333333 341.333333 341.333333 247.04 417.706667 170.666667 512 170.666667M512 597.333333C700.586667 597.333333 853.333333 673.706667 853.333333 768L853.333333 853.333333 170.666667 853.333333 170.666667 768C170.666667 673.706667 323.413333 597.333333 512 597.333333Z"  ></path>'+
      ''+
    '</symbol>'+
  ''+
    '<symbol id="icon-accountcircle" viewBox="0 0 1024 1024">'+
      ''+
      '<path d="M512 819.2C405.333333 819.2 311.04 764.586667 256 682.666667 257.28 597.333333 426.666667 550.4 512 550.4 597.333333 550.4 766.72 597.333333 768 682.666667 712.96 764.586667 618.666667 819.2 512 819.2M512 213.333333C582.826667 213.333333 640 270.506667 640 341.333333 640 412.16 582.826667 469.333333 512 469.333333 441.173333 469.333333 384 412.16 384 341.333333 384 270.506667 441.173333 213.333333 512 213.333333M512 85.333333C276.48 85.333333 85.333333 276.48 85.333333 512 85.333333 747.52 276.48 938.666667 512 938.666667 747.52 938.666667 938.666667 747.52 938.666667 512 938.666667 276.053333 746.666667 85.333333 512 85.333333Z"  ></path>'+
      ''+
    '</symbol>'+
  ''+
    '<symbol id="icon-cellphoneandroid" viewBox="0 0 1024 1024">'+
      ''+
      '<path d="M736 768 288 768 288 170.666667 736 170.666667M597.333333 896 426.666667 896 426.666667 853.333333 597.333333 853.333333M682.666667 42.666667 341.333333 42.666667C270.506667 42.666667 213.333333 99.84 213.333333 170.666667L213.333333 853.333333C213.333333 924.16 270.506667 981.333333 341.333333 981.333333L682.666667 981.333333C753.493333 981.333333 810.666667 924.16 810.666667 853.333333L810.666667 170.666667C810.666667 99.84 753.493333 42.666667 682.666667 42.666667Z"  ></path>'+
      ''+
    '</symbol>'+
  ''+
    '<symbol id="icon-lock" viewBox="0 0 1024 1024">'+
      ''+
      '<path d="M512 725.333333C558.933333 725.333333 597.333333 686.933333 597.333333 640 597.333333 592.64 558.933333 554.666667 512 554.666667 465.066667 554.666667 426.666667 593.066667 426.666667 640 426.666667 686.933333 465.066667 725.333333 512 725.333333M768 341.333333C814.933333 341.333333 853.333333 379.733333 853.333333 426.666667L853.333333 853.333333C853.333333 900.266667 814.933333 938.666667 768 938.666667L256 938.666667C209.066667 938.666667 170.666667 900.266667 170.666667 853.333333L170.666667 426.666667C170.666667 379.306667 209.066667 341.333333 256 341.333333L298.666667 341.333333 298.666667 256C298.666667 138.24 394.24 42.666667 512 42.666667 629.76 42.666667 725.333333 138.24 725.333333 256L725.333333 341.333333 768 341.333333M512 128C441.173333 128 384 185.173333 384 256L384 341.333333 640 341.333333 640 256C640 185.173333 582.826667 128 512 128Z"  ></path>'+
      ''+
    '</symbol>'+
  ''+
    '<symbol id="icon-lockoutline" viewBox="0 0 1024 1024">'+
      ''+
      '<path d="M512 725.333333C464.64 725.333333 426.666667 686.933333 426.666667 640 426.666667 592.64 464.64 554.666667 512 554.666667 558.933333 554.666667 597.333333 593.066667 597.333333 640 597.333333 686.933333 558.933333 725.333333 512 725.333333M768 853.333333 768 426.666667 256 426.666667 256 853.333333 768 853.333333M768 341.333333C814.933333 341.333333 853.333333 379.733333 853.333333 426.666667L853.333333 853.333333C853.333333 900.266667 814.933333 938.666667 768 938.666667L256 938.666667C208.64 938.666667 170.666667 900.266667 170.666667 853.333333L170.666667 426.666667C170.666667 379.306667 208.64 341.333333 256 341.333333L298.666667 341.333333 298.666667 256C298.666667 138.24 394.24 42.666667 512 42.666667 629.76 42.666667 725.333333 138.24 725.333333 256L725.333333 341.333333 768 341.333333M512 128C441.173333 128 384 185.173333 384 256L384 341.333333 640 341.333333 640 256C640 185.173333 582.826667 128 512 128Z"  ></path>'+
      ''+
    '</symbol>'+
  ''+
    '<symbol id="icon-shield" viewBox="0 0 1024 1024">'+
      ''+
      '<path d="M512 42.666667 128 213.333333 128 469.333333C128 706.133333 291.84 927.573333 512 981.333333 732.16 927.573333 896 706.133333 896 469.333333L896 213.333333 512 42.666667Z"  ></path>'+
      ''+
    '</symbol>'+
  ''+
    '<symbol id="icon-shieldoutline" viewBox="0 0 1024 1024">'+
      ''+
      '<path d="M896 469.333333C896 706.133333 732.16 927.573333 512 981.333333 291.84 927.573333 128 706.133333 128 469.333333L128 213.333333 512 42.666667 896 213.333333 896 469.333333M512 896C672 853.333333 810.666667 663.04 810.666667 478.72L810.666667 268.8 512 135.68 213.333333 268.8 213.333333 478.72C213.333333 663.04 352 853.333333 512 896Z"  ></path>'+
      ''+
    '</symbol>'+
  ''+
'</svg>'
var script = function() {
    var scripts = document.getElementsByTagName('script')
    return scripts[scripts.length - 1]
  }()
var shouldInjectCss = script.getAttribute("data-injectcss")

/**
 * document ready
 */
var ready = function(fn){
  if(document.addEventListener){
      document.addEventListener("DOMContentLoaded",function(){
          document.removeEventListener("DOMContentLoaded",arguments.callee,false)
          fn()
      },false)
  }else if(document.attachEvent){
     IEContentLoaded (window, fn)
  }

  function IEContentLoaded (w, fn) {
      var d = w.document, done = false,
      // only fire once
      init = function () {
          if (!done) {
              done = true
              fn()
          }
      }
      // polling for no errors
      ;(function () {
          try {
              // throws errors until after ondocumentready
              d.documentElement.doScroll('left')
          } catch (e) {
              setTimeout(arguments.callee, 50)
              return
          }
          // no errors, fire

          init()
      })()
      // trying to always fire before onload
      d.onreadystatechange = function() {
          if (d.readyState == 'complete') {
              d.onreadystatechange = null
              init()
          }
      }
  }
}

/**
 * Insert el before target
 *
 * @param {Element} el
 * @param {Element} target
 */

var before = function (el, target) {
  target.parentNode.insertBefore(el, target)
}

/**
 * Prepend el to target
 *
 * @param {Element} el
 * @param {Element} target
 */

var prepend = function (el, target) {
  if (target.firstChild) {
    before(el, target.firstChild)
  } else {
    target.appendChild(el)
  }
}

function appendSvg(){
  var div,svg

  div = document.createElement('div')
  div.innerHTML = svgSprite
  svg = div.getElementsByTagName('svg')[0]
  if (svg) {
    svg.setAttribute('aria-hidden', 'true')
    svg.style.position = 'absolute'
    svg.style.width = 0
    svg.style.height = 0
    svg.style.overflow = 'hidden'
    prepend(svg,document.body)
  }
}

if(shouldInjectCss && !window.__iconfont__svg__cssinject__){
  window.__iconfont__svg__cssinject__ = true
  try{
    document.write("<style>.svgfont {display: inline-block;width: 1em;height: 1em;fill: currentColor;vertical-align: -0.1em;font-size:16px;}</style>");
  }catch(e){
    console && console.log(e)
  }
}

ready(appendSvg)


})(window)
