import React, { Component } from 'react';
import {BrowserRouter, Switch, Route} from "react-router-dom";
import {ProductList,Product} from 'oodt_fm_plugin_sample';
import OPSUIHome from "./components/OPSUIHome";
import SearchBar from "./components/SearchBar";


class MyApp extends Component {


  constructor(props) {
    super(props);
    this.setSelectedProductId=this.setSelectedProductId.bind(this);
  }

  state = {
    selectedProductId:'',
  };

  setSelectedProductId(productId) {
    this.setState({selectedProductId: productId});
    console.log(this.state.selectedProductId);
  };

  render() {
    return (
        <BrowserRouter>
          <OPSUIHome>
            <Switch>

              <Route exact path={"/"} render={() =>
                  <div>
                    <h1>Dashboard</h1>
                  </div>
              }/>

              <Route path={"/products"} render={() =>
                  <ProductList selectedProductId={this.setSelectedProductId}/>

              }/>

              <Route path={"/product"} render={() =>
                  <div>
                    <SearchBar setSelectedProductId={this.setSelectedProductId}/>
                    <br/>
                    <Product productId={this.state.selectedProductId}/>
                  </div>
              }/>

            </Switch>
          </OPSUIHome>
        </BrowserRouter>
    );
  }
}

export default MyApp;