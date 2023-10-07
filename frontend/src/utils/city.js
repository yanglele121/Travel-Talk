export function formatcity(city){
    let d = city.split('·')[1]
    return d;
}

export function formatProvince(addr){
    let d = addr
    if(d==="上海"||d==="天津"||d==="重庆"||d==="北京"){
        d=d+"市"
    }
    else if(d.length<=3){
        d=d+"省"
    }
    return d;
}
