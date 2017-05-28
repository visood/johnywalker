import React from 'react';
import ReactDOM from 'react-dom';
import SunWeatherComponent from './sun-weather-component.jsx';

console.log("in main.js");
ReactDOM.render(<SunWeatherComponent />, document.getElementById('reactView'));
