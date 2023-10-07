
export function formatStarCounts(data){
    let d = data
    if(data>10000){
        d = d /10000;
        return d.toFixed(2)+"w"
    }
    return d;
}