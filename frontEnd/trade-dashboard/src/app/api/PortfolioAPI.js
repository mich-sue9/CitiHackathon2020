

const url = "http://localhost:8080/api/portfolios/";
export const portId = "5f628e0a113d5610c2ccea84";


export const getPortfolio = function(portfolioId){
    return fetch(url+portfolioId);
}

export const getStockList = function(portfolioId){
    return fetch(url+"getStockLivePrice/"+portfolioId);
}