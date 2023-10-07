import request from '@/request'

export function AllpicAlbums(pageparam){
    return request({
        url:'/picAlbum/picAlbumList',
        method: 'post',
        data:pageparam
    })
}

export function NewPicAlbum(PicInfo,token){
    return request({
        headers: {'Authorization': token},
        url: '/picAlbum/addPicAlbum',
        method: "post",
        data: PicInfo
    })
}