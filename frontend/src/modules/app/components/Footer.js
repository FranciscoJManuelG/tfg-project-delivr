import React from 'react';
import {FormattedMessage} from 'react-intl';

const Footer = () => (

    <div>
        <br/>
        <hr/>
        <footer className="navbar-fixed-bottom">
            <p className="text-center">
                <FormattedMessage id="project.app.Footer.text"/>
            </p>
        </footer>
    </div>

);

export default Footer;
