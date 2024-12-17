const express = require('express');
const bodyParser = require('body-parser');
const axios = require('axios');
const cors = require('cors');
require('dotenv').config();

const app = express();
const PORT = 3000;

const modulePrices = [
    {id: 1, price: 6.99},
    {id: -1, price: 6.99},
]

// Middleware
app.use(bodyParser.json());
app.use(cors()); // Erlaubt Cross-Origin-Anfragen

// PayPal-API-URLs
const PAYPAL_API = process.env.PAYPAL_API || 'https://api-m.sandbox.paypal.com';
const BACKEND_API = process.env.BACKEND_API || 'http://localhost:8080/';
const CLIENT_ID = process.env.PAYPAL_CLIENT_ID;
const CLIENT_SECRET = process.env.PAYPAL_CLIENT_SECRET;

// 1. PayPal Access Token abrufen
async function getAccessToken() {
    const auth = Buffer.from(`${CLIENT_ID}:${CLIENT_SECRET}`).toString('base64');
    const response = await axios.post(`${PAYPAL_API}/v1/oauth2/token`, 'grant_type=client_credentials', {
        headers: {
            Authorization: `Basic ${auth}`,
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    });

    return response.data.access_token;
}

// 2. Bestellung erstellen
app.post('/paypal/create-order', async (req, res) => {
    try {
        const { modules, username } = req.body;

        // Validierung der Eingabe
        if (!modules || modules.length === 0) {
            console.error("Modules not provided");
            return res.status(400).json({ error: "Modules not provided" });
        }

        if (!username || username === "") {
            console.error("Github-Username not provided");
            return res.status(400).json({ error: "Github-Username not provided" });
        }

        // Berechnung des Gesamtpreises
        const totalPrice = modules.reduce((sum, moduleId) => {
            const modulePrice = modulePrices.find(price => price.id === moduleId);
            if (!modulePrice) {
                console.error(`Module with ID ${moduleId} not found`);
                throw new Error(`Module with ID ${moduleId} not found`);
            }
            return sum + modulePrice.price;
        }, 0);

        if (totalPrice <= 0) {
            console.error("Total price is invalid");
            return res.status(400).json({ error: "Total price is invalid" });
        }

        const accessToken = await getAccessToken();
        const order = {
            'intent': 'CAPTURE',
            'purchase_units': [
                {
                    'amount': {
                        'currency_code': 'USD',
                        'value': totalPrice,
                    },
                },
            ],
        };

        const response = await axios.post(`${PAYPAL_API}/v2/checkout/orders`, order, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
        });
        console.log("Hello")

        res.json(response.data); // Sendet die Bestellinformationen zurück
    } catch (error) {
        console.error(error.response?.data || error.message);
        res.status(500).send('Fehler beim Erstellen der Bestellung');
    }
});

// 3. Bestellung abschließen
app.post('/paypal/capture-order', async (req, res) => {
    const { orderId, modules, githubUserName } = req.body; // Bestell-ID von der Anfrage abrufen

    try {
        const accessToken = await getAccessToken();
        const response = await axios.post(
            `${PAYPAL_API}/v2/checkout/orders/${orderId}/capture`,
            {},
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                    'Content-Type': 'application/json',
                },
            }
        );


        // Validierung der Eingabe
        if (!modules || modules.length === 0) {
            console.error("Modules not provided");
            return res.status(400).json({ error: "Modules not provided" });
        }

        if (!githubUserName || githubUserName === "") {
            console.error("Github-Username not provided");
            return res.status(400).json({ error: "Github-Username not provided" });
        }

        // Berechnung des Gesamtpreises
        const totalPrice = modules.reduce((sum, moduleId) => {
            const modulePrice = modulePrices.find(price => price.id === moduleId);
            if (!modulePrice) {
                console.error(`Module with ID ${moduleId} not found`);
                throw new Error(`Module with ID ${moduleId} not found`);
            }
            return sum + modulePrice.price;
        }, 0);

        if (totalPrice <= 0) {
            console.error("Total price is invalid");
            return res.status(400).json({ error: "Total price is invalid" });
        }


        await axios.post(`${BACKEND_API}license/add`, {ghUserName: githubUserName, module: modules, pricePayed: totalPrice, orderDate: Date.now()}, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
        });

        res.json(response.data); // Sendet die Transaktionsdetails zurück
    } catch (error) {
        console.error(error.response?.data || error.message);
        res.status(500).send('Fehler beim Abschließen der Bestellung');
    }
});

// Start des Servers
app.listen(PORT, () => {
    console.log(`Server läuft auf http://localhost:${PORT}`);
});