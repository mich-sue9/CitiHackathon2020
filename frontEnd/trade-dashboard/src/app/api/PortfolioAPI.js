function getUrl(){
    return "'http://localhost:8080/api/portfolios";
}

export const getPortfolio = function(portfolioId){
    return fetch(getUrl()+portfolioId);
}