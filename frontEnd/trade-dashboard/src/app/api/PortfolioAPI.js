const url = "http://localhost:8080/api/portfolios/";

export const getPortfolio = function(portfolioId){
    return fetch(url+portfolioId);
}

export const getStockList = function(portfolioId){
    return fetch(url+"getStockLivePrice/"+portfolioId);
}