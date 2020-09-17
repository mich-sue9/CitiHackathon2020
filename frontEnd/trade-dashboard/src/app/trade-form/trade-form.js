import React, { Component } from 'react';
import { Form } from 'react-bootstrap';
import {portId} from '../api/PortfolioAPI';



export class TradeForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      startDate: new Date(),
      tradeSent: false,
      ticker: "",
      quantity: 0,
      requestPrice: 0,
      tStatus: "CREATED",
      portfolioId: portId,
      isLoaded: false,

      trades: []
    };

    this.tickerNameUpload = this.tickerNameUpload.bind(this);
    this.quantityUpload = this.quantityUpload.bind(this);
    this.requestPriceUpload = this.requestPriceUpload.bind(this);
    this.handleOrderTrade = this.handleOrderTrade.bind(this);
  }


  handleChange = date => {
    this.setState({
      startDate: date
    });
  };



  tickerNameUpload = e => {
    this.setState({ ticker: e.target.value });
  };

  quantityUpload = e => {
    this.setState({ quantity: e.target.value });
  };

  requestPriceUpload = e => {
    this.setState({ requestPrice: e.target.value });
  };

  handleOrderTrade(value) {
    let trade = {};
    trade.stockTicker = this.state.ticker;
    if (value == "buy"){
      trade.quantity = this.state.quantity;
    } else {
      trade.quantity = -this.state.quantity;
    }
    trade.requestPrice = this.state.requestPrice;
    trade.tStatus = this.state.tStatus;
    let response = fetch("http://localhost:8080/api/trades/addTrade/" + this.state.portfolioId, {
      method: "POST",
      headers: {
        'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(trade)
    });

    response.then(res => res.json()).then(result => { this.setState({ isLoaded: true }); }, error => { this.setState({ isLoaded: false }) });
  }




  loadPendingTrades = () => {
    let response = fetch('http://localhost:8080/api/portfolios/pending/' + this.state.portfolioId);
    response
      .then(res => res.json())
      .then(
        result => {
          this.setState({
            isLoaded: true,
            trades: result,
          });
        },
        error => {
          this.setState({
            isLoaded: false,
          });
        }
      );
  }


  componentDidMount() {
    //bsCustomFileInput.init()
    this.loadPendingTrades();
    this.pendingTradeRefresher = setInterval(() => {
      this.loadPendingTrades();
    }, 5000)
  }

  componentWillUnmount() {
    clearInterval(this.pendingTradeRefresher)
  }



  render() {
    return (
      <div>

        <div className="page-header">
          <h3 className="page-title"> Trade Submission </h3>
          <nav aria-label="breadcrumb">
            <ol className="breadcrumb">
              <li className="breadcrumb-item active" aria-current="page">Trade Sumbit</li>
            </ol>
          </nav>
        </div>
        <div className="row">
          <div className="col-12 grid-margin stretch-card">
            <div className="card">
              <div className="card-body">
                <h4 className="card-title">Trade request</h4>
                <p className="card-description">  </p>
                <form className="forms-sample">
                  <Form.Group>
                    <label htmlFor="exampleInputUsername1">Ticker</label>
                    <Form.Control type="text" id="exampleInputUsername1" placeholder="Ticker" size="lg" onChange={this.tickerNameUpload} required />
                  </Form.Group>
                  <Form.Group>
                    <label htmlFor="exampleInputEmail1">Quantity</label>
                    <Form.Control type="test" className="form-control" id="exampleInputEmail1" placeholder="Quantity" onChange={this.quantityUpload} required />
                  </Form.Group>
                  <Form.Group>
                    <label htmlFor="exampleInputPassword1">Request Price</label>
                    <Form.Control type="text" className="form-control" id="exampleInputPassword1" placeholder="Request Price" onChange={this.requestPriceUpload} required />
                  </Form.Group>

                  <button type="submit" className="btn btn-gradient-primary mr-2" value="buy" onClick={e => this.handleOrderTrade(e.target.value)} >Buy</button>
                  <button type="submit" className="btn btn-gradient-primary mr-2" value="sell" onClick={e => this.handleOrderTrade(e.target.value)} >Sell</button>
                  <button className="btn btn-light">Cancel</button>
                </form>
              </div>
            </div>
          </div>
        </div>
        <div className="row">
          <div className="col-lg-12 grid-margin stretch-card">
            <div className="card">
              <div className="card-body">
                <h4 className="card-title">Pending Trades</h4>
                <div className="table-responsive">
                  <table className="table table-bordered">
                    <thead>

                      <tr>
                        <th> Date </th>
                        <th> Ticker </th>
                        <th> Quantity </th>
                        <th> Price </th>
                        <th> Status </th>
                        <th> Total Investment </th>
                      </tr>
                    </thead>
                    <tbody>
                      {this.state.trades.map((trade) =>
                        <tr>
                          <td>{trade.dateCreated.substring(0,10)} {trade.dateCreated.substring(11,19)}</td>
                          <td>{trade.stockTicker} </td>
                          <td>{trade.quantity}</td>

                          <td>${trade.requestPrice}</td>

                          <td><label className="badge badge-info">{trade.tStatus}</label></td>
                          <td>${trade.requestPrice * trade.quantity}</td>
                        </tr>

                      )
                      }


                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>


        </div>
      </div>
    )
  }
}

export default TradeForm
