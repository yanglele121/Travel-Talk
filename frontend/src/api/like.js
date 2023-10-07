import request from '@/request'

export function addCommentLike(commentLikeData) {
    return request({
        url: '/like/addCommentLike',
        method: 'post',
        data : commentLikeData
    })
}

export function delCommentLike(commentLikeData) {
    return request({
        url: '/like/delCommentLike',
        method: 'post',
        data : commentLikeData
    })
}

export function addArticleLike(articleLikeData) {
    return request({
        url: '/like/addArticleLike',
        method: 'post',
        data : articleLikeData
    })
}

export function delArticleLike(articleLikeData) {
    return request({
        url: '/like/delArticleLike',
        method: 'post',
        data : articleLikeData
    })
}

export function addArticleCollect(articleCollectData) {
    return request({
        url: '/like/addArticleCollect',
        method: 'post',
        data : articleCollectData
    })
}
export function addPicAlbumLike(picAlbumLikeData) {
    return request({
        url: '/like/addPicAlbumLike',
        method: 'post',
        data : picAlbumLikeData
    })
}
//5-22
export function delPicAlbumLike(picAlbumLikeData) {
    return request({
        url: '/like/delPicAlbumLike',
        method: 'post',
        data : picAlbumLikeData
    })
}

export function delArticleCollect(articleCollectData) {
    return request({
        url: '/like/delArticleCollect',
        method: 'post',
        data : articleCollectData
    })
}

export function getCollectArticleListByUserId(userId,pageparam) {
    return request({
        url: `/like/getCollectArticleListByUserId/${userId}`,
        method: 'post',
        data : pageparam
    })
}

export function checkPicAlbumLike(picAlbumLikeData) {
    return request({
        url: '/like/checkPicAlbumLike',
        method: 'post',
        data : picAlbumLikeData
    })
}

export function getCollectSiteListByUserId(userId,pageparam) {
    return request({
        url: `/like/getCollectSiteListByUserId/${userId}`,
        method: 'post',
        data : pageparam
    })
}