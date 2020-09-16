import React, { Component } from 'react';
import { Form } from 'react-bootstrap';
import DatePicker from "react-datepicker";
import bsCustomFileInput from 'bs-custom-file-input'

export class TradeForm extends Component {
  constructor(props){
    super(props);
    this.state = {
      startDate: new Date(),
      tradeSent: false,
      ticker: "",
      quantity: 0,
      requestPrice: 0,
      portfolioId: "5f611f1e06a0cd1e3dd491dc",
      isLoaded: false
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
  componentDidMount() {
    bsCustomFileInput.init()
  }

  tickerNameUpload = e => {
    this.setState({ticker: e.target.value});
  };

  quantityUpload = e => {
    this.setState({quantity: e.target.value});
  };

  requestPriceUpload = e => {
    this.setState({requestPrice: e.target.value});
  };

  handleOrderTrade(){
    let trade = {}; 
    trade.stockTicker= this.state.ticker;
    trade.quantity = this.state.quantity;
    trade.requestPrice = this.state.requestPrice;
    let response = fetch('http://localhost:8080/' + "api/trades/addTrade/" + this.state.portfolioId, {
      method: "POST",
      headers :{
          'Content-Type': 'application/json;charset=utf-8'
      },
      body: JSON.stringify(trade)});

    response.then(res => res.json()).then(result => {this.setState({isLoaded:true}); }, error => {this.setState({isLoaded:false})} );
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
                    <Form.Control type="text" id="exampleInputUsername1" placeholder="Ticker" size="lg" onChange={this.tickerNameUpload} required/>
                  </Form.Group>
                  <Form.Group>
                    <label htmlFor="exampleInputEmail1">Quantity</label>
                    <Form.Control type="test" className="form-control" id="exampleInputEmail1" placeholder="Quantity" onChange={this.quantityUpload} required/>
                  </Form.Group>
                  <Form.Group>
                    <label htmlFor="exampleInputPassword1">Request Price</label>
                    <Form.Control type="text" className="form-control" id="exampleInputPassword1" placeholder="Request Price" onChange={this.requestPriceUpload} required/>
                  </Form.Group>

                  <button type="submit" className="btn btn-gradient-primary mr-2" onClick={this.handleOrderTrade} >Submit</button>
                  <button className="btn btn-light">Cancel</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default TradeForm
