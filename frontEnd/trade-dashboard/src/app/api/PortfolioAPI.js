

const url = "http://localhost:8080/api/portfolios/";
export const portId = "5f63644b86ac153942fd1cc1";

export const getPortfolio = function(portfolioId){
    return fetch(url+portfolioId);
}

export const getStockList = function(portfolioId){
    return fetch(url+"getStockLivePrice/"+portfolioId);
}