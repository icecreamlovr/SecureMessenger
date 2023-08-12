var path = require('path');

module.exports = {
    entry: {
        app: './src/main/js/app.js',
        registration: './src/main/js/registration.js',
        login: './src/main/js/login.js'
    },
    devtool: 'source-map',
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/react-[name].js'
    },
    module: {
        rules: [{
            test: path.join(__dirname, '.'),
            exclude: /node_modules/,
            loader: "babel-loader",
            options: {
                presets: ['@babel/preset-env', '@babel/preset-react']
            }
        }]
    },
    resolve: {
        extensions: ['.js', '.jsx']
    }
};
