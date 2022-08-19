<template>
  <div id="modal-terms">
    <div class="modal-content modal-policy">
      <div class="modal-header">
        <div class="modal-title">Terms of Service <small>(VER 1.0)</small></div>
        <div class="modal-elements">
          <div class="dropdown">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">English</button>
            <div class="dropdown-menu dropdown-menu-right">
              <button type="button" class="dropdown-item active" href="#">English</button>
              <button type="button" class="dropdown-item" href="#">한국어</button>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-body">
        <div class="policy-content-wrap">
          <div class="policy-content-inner overflow-y-scroll">
            <div class="section">
              <div class="shortcut-box">
                <p>Table of Contents</p>
                <div class="row shortcut-list">
                  <div class="col-md-6">
                    <ol class="list-style-decimal" start="1">
                      <li @click="scrollToSection('ts-s1')"><a>Account Creation and Eligibility</a></li>
                      <li @click="scrollToSection('ts-s2')"><a>Use of the Website and Services</a></li>
                      <li @click="scrollToSection('ts-s3')"><a>Ownership; Proprietary Right</a></li>
                      <li @click="scrollToSection('ts-s4')"><a>Fees and Payment Terms</a></li>
                      <li @click="scrollToSection('ts-s5')"><a>Confidentiality</a></li>
                      <li @click="scrollToSection('ts-s6')"><a>Third-Party Contents</a></li>
                    </ol>
                  </div>
                  <div class="col-md-6">
                    <ol class="list-style-decimal" start="7">
                      <li @click="scrollToSection('ts-s7')"><a>Disclaimer</a></li>
                      <li @click="scrollToSection('ts-s8')"><a>Indemnification</a></li>
                      <li @click="scrollToSection('ts-s9')"><a>Limitation of Liability</a></li>
                      <li @click="scrollToSection('ts-s10')"><a>Termination</a></li>
                      <li @click="scrollToSection('ts-s11')"><a>General</a></li>
                    </ol>
                  </div>
                </div>
              </div>
              <p>
                These Terms of Service (”Terms”) is entered into by and between you and DeepSignal, LLC. as well as DeepSignal’s parents,
                affiliates, and subsidiaries. (“DeepSignal,” “we,” or “us”). The terms “you,” “your,” or “User” refer to you or a company,
                organization, or other entity on whose behalf you agree to these Terms. These Terms govern your access to and use of our
                services on our website deepsignal.ai (“Website”) and all other services including, without limitation, services on a trial
                basis, beta services, or services that are free of charge (“Services”).
              </p>
              <p>
                These Terms limit our liability to you and include a class action waiver and waiver of jury trial. Disputes related to these
                terms must be settled through a dispute resolution process which may lead to binding arbitration. For more details, see
                section 9 (<a @click="scrollToSection('ts-s9')">Limitation of Liability</a>) and section 11.10 (<a
                  @click="scrollToSection('ts-s10')"
                  >Dispute Resolution</a
                >).
              </p>
              <p>
                Beside these terms, we also have additional terms, conditions, and policies that apply to your use of the Website and
                Services (“Supplemental Terms”) which are hereby incorporated by reference into these Terms. DeepSignal can modify these
                Terms or service-related policies at any time. We will provide written notice of any material updates at least thirty (30)
                days before the effective date unless the changes result from changes in laws or regulations. Updated Terms and policies
                will be effective and binding upon the date indicated on the top of each document. Your continued use of the Website and
                Services following changes constitutes your agreement to such changes. You must stop using our Website or Services if you do
                not agree to the changes. You can find the updated Terms and Supplemental Terms at the links below:
              </p>
              <div class="table-wrap">
                <table class="table">
                  <tbody>
                    <tr>
                      <th>Terms of Service Last Updated</th>
                      <td>May 1, 2022</td>
                    </tr>
                    <tr>
                      <th>Privacy Policy</th>
                      <td>Document that describes how DeepSignal collects, uses, and shares data in our Website and Services</td>
                    </tr>
                    <tr>
                      <th>Personal Information Collection and Terms of Use</th>
                      <td>Document that allows DeepSignal to collect your personal information</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <p>
                Please read these Terms carefully. Once accepted, these Terms become a binding legal contract between you and DeepSignal. By
                checking the “I agree” box, you represent that
              </p>
              <ol class="list-style-lowerAlpha">
                <li>you have read, understand, and agree to be bound by these Terms,</li>
                <li>you are of legal age in your jurisdiction of residence to form a binding contract with DeepSignal, and</li>
                <li>
                  you are entering into these Terms personally, or have the authority to enter into these Terms on behalf of any company,
                  organization, or other legal entity on whose behalf you use the Website or Services.
                </li>
              </ol>
              <p>
                If you do not agree to be bound by these terms, you should not accept these Terms, create an account, or use our Website or
                Services.
              </p>
            </div>
            <div class="section" id="ts-s1">
              <h2>1. Account Creation and Eligibility</h2>
              <p>
                To use our Website or Services, you must create an account. Before creating an account, you are responsible for ensuring
                that the use of our Website or Services complies with all laws and regulations applicable to you. When creating an account,
                you will be asked to provide your name and an email address or phone number. The information you provide us will be subject
                to our Privacy Policy. You must provide true, accurate, current, and complete information about yourself. If you breach
                these Terms, you are prohibited from creating new accounts and we may, at our sole discretion, deactivate or terminate your
                account and block access to our Website and Services.
              </p>
            </div>
            <div class="section" id="ts-s2">
              <h2>2. Use of the Website and Services</h2>
              <p>
                We own all rights, title and interest of our Website and Services. The Website and Services are protected by copyright laws
                and other intellectual property laws in the United State and throughout the world. Subject to these Terms, we grant you a
                limited, non-exclusive, non-transferable, non-sub-licensable license to use the Website and Services solely for your own
                non-commercial purposes. Any future, release, update, or other modification to the Website or Services shall be subject to
                these Terms. We reserve all right not granted in these Terms.
              </p>
              <ol>
                <li>
                  <h3>2.1 Our Responsibilities</h3>
                  <p>Subject to these Terms, we will</p>
                  <ol class="list-style-lowerAlpha">
                    <li>make the Services available to you in accordance with these Terms and our Supplemental Terms;</li>
                    <li>provide the Services in accordance with laws applicable to our provision of the Services to you;</li>
                    <li>
                      make reasonable efforts to use industry standard measures to scan, detect, and delete code, files, scripts, agents, or
                      programs intended to do harm, including viruses, worms, time bombs, and Trojan horses;
                    </li>
                    <li>make reasonable efforts to provide you with adequate support for the Website and Services.</li>
                  </ol>
                </li>
                <li>
                  <h3>2.2 Service Restrictions</h3>
                  <p>
                    You must use the Website and Services only in accordance with these Terms, Supplemental Terms, and applicable laws and
                    regulations. The rights granted to you in these Terms are subject to following restrictions: you shall not
                  </p>
                  <ol class="list-style-lowerAlpha">
                    <li>
                      sell, rent, lease, transfer, lease or otherwise commercially exploit the Website or Services or any portion of the
                      Website or Services;
                    </li>
                    <li>
                      frame or enclose any trademark, logo of the Website or Services including, without limitation, images, text, scripts,
                      page layout or form;
                    </li>
                    <li>use any metatags or other “hidden text” using our name or trademarks;</li>
                    <li>
                      modify, translate, adapt, merge, disassemble, make derivative works of, or reverse engineer any part of the Website or
                      Services;
                    </li>
                    <li>
                      use any automated or manual software, devices, or other processes to scrape or download data from the Website or
                      Services;
                    </li>
                    <li>use the Website or Services to build a similar or competitive website, application, or service;</li>
                    <li>remove or destroy any copyright notices or other proprietary markings within the Website or Services.</li>
                  </ol>
                </li>
                <li>
                  <h3>2.3 Unauthorized Use</h3>
                  <p>You must not, under any circumstances:</p>
                  <ol class="list-style-lowerAlpha">
                    <li>interfere with the proper functioning of the Website or Services;</li>
                    <li>tampering with or circumventing any security or usage rules embedded in the Website or Services;</li>
                    <li>
                      harvesting or collecting contact information of other users from the Website or Services by electronic or other means;
                    </li>
                    <li>
                      disrupt, overburden, or aid in the disruption or overburdening of any computer, server, or other infrastructure used
                      to offer or support our Website or Services;
                    </li>
                    <li>attempt to gain unauthorized access to the Website or Services;</li>
                    <li>
                      access, tamper, or use non-public areas of the Website, Services, our computer systems, or technical delivery systems
                      of our providers;
                    </li>
                    <li>
                      attempt to probe, scan, or test the vulnerability of any of our system or network, or breach any security or
                      authentication measures;
                    </li>
                    <li>
                      disrupt or interfere with the security of, or otherwise cause harm to the Website, Services, systems, resources,
                      accounts, passwords, servers or networks connected to or accessible through our Website or Services. Any unauthorized
                      use of the Website or Services terminates the licenses granted to you in accordance with these Terms.
                    </li>
                  </ol>
                </li>
              </ol>
            </div>
            <div class="section" id="ts-s3">
              <h2>3. Ownership; Proprietary Right</h2>
              <p>
                We exclusively own and reserve all right, title and interested in the Website and Services. Any elements of the Website or
                Services, including, without limitation, visual interfaces, graphics, design, images, compilation, information, data,
                computer code, software provided by us are protected by intellectual property and other laws. You must not remove, alter, or
                obscure any copyright, trademark, service mark, or other proprietary rights notices incorporated in the Website or Services.
                All materials included in the Website or Services are our property or our third-party licensors. We reserve all rights not
                expressly granted under these Terms.
              </p>
              <ol>
                <li>
                  <h3>3.1 Feedback</h3>
                  <p>
                    By submitting any ideas, suggestions, or proposals to us through our suggestion/feedback pages or other similar pages
                    (“Feedback”), you grant us a fully paid, royalty-free, perpetual, irrevocable, non-exclusive, and fully sublicensable
                    right to use, reproduce, and otherwise commercially or non-commercially exploit your Feedback in any manner.
                  </p>
                </li>
              </ol>
            </div>
            <div class="section" id="ts-s4">
              <h2>4. Fees and Payment Terms</h2>
              <ol>
                <li>
                  <h3>4.1 Fees</h3>
                  <p>
                    When you purchase any of our paid Services, you agree to pay all fees and/or charges to your account in accordance with
                    the fees, charges, and billing terms in effect at the time the fee and/or charge is due and payable. You must provide us
                    a valid credit card information and by providing us with your credit card number and other information necessary for
                    processing the payment, you agree that we are authorized to immediately invoice your account for all fees and/or charges
                    due and payable to us and that no additional notice or consent is required. Your agreement with the payment providers
                    governs your use of the designated credit card, and you must refer to that agreement and not these Terms to determine
                    your rights and liabilities. Recurring license charges are billed in advance in the frequency set forth in the order
                    form, and usage-based and one-time charges are billed monthly in areears.
                  </p>
                </li>
                <li>
                  <h3>4.2 Taxes</h3>
                  <p>
                    All fees are exclusive of any applicable taxes, levies, duties, or other similar exactions imposed by a legal,
                    governmental, or regulatory authority in any applicable jurisdiction, including, without limitation, sales, use,
                    value-added, consumption, communications, or withholding taxes (“Taxes”). You will pay all Taxes associated with these
                    Terms, excluding any taxes based on our net income, property, or employees. If you are required by law to withhold any
                    Taxes from payments owed to us, you will reduce or eliminate such withheld Taxes upon receipt of the appropriate tax
                    certificates or document provided by us. You will provide us with proof of payment of any withheld Taxes to the
                    appropriate authority. Taxes will be shown on a separate line item on your invoice.
                  </p>
                </li>
                <li>
                  <h3>4.3 Price Changes</h3>
                  <p>
                    If we change the fees for the Services, including by adding additional fees or charges, we will provide you advance
                    notice of those changes. If you do not accept the changes, we reserve the right to discontinue providing the Services to
                    you.
                  </p>
                </li>
                <li>
                  <h3>4.4 Late Payments; Non-payments; Suspension</h3>
                  <p>
                    We reserve the right to deactivate, terminate, prevent access to, disable services for your account and prevent access
                    to the Website and Services at any time at our sole discretion, including for nonpayment, late payment, or failure to
                    charge your payment methods upon invoicing.
                  </p>
                </li>
              </ol>
            </div>
            <div class="section" id="ts-s5">
              <h2>5. Confidentiality</h2>
              <ol>
                <li>
                  <h3>5.1 Definition of Confidential Information</h3>
                  <p>
                    Any nonpublic information from us that is designated confidential or, given the nature of the information or
                    circumstances surrounding its disclosure, reasonably should be understood to be confidential (“Confidential
                    Information”) must be handled according to these Terms.
                  </p>
                  <p>Confidential Information includes but is not limited to:</p>
                  <ol class="list-style-lowerAlpha">
                    <li>
                      nonpublic information about our technology, customers, business plans, marketing and sales activities, finances,
                      operations and other business information; and
                    </li>
                    <li>
                      the existence and content of our discussions or negotiations with you regarding your account or use of the Website or
                      Services.
                    </li>
                  </ol>
                  <p>Confidential Information does not include any information that:</p>
                  <ol class="list-style-lowerAlpha">
                    <li>is or becomes publicly available without breach of these Terms;</li>
                    <li>can be shown by documentation to have been known to you at the time of your receipt from us;</li>
                    <li>
                      is received from a third party who did not acquire or disclose the information by a wrongful or tortious act or by
                      violating the rights of us or third parties; or
                    </li>
                    <li>
                      can be shown by documentation to have been independently developed by you without reference to the Confidential
                      Information.
                    </li>
                  </ol>
                </li>
                <li>
                  <h3>5.2 Confidentiality</h3>
                  <p>
                    You shall not disclose or use any Confidential Information for any purpose outside the scope of these Terms unless you
                    have our express consent. We may disclose Confidential Information to our personnel, auditors, accountants, attorneys or
                    advisors who are subject to confidentiality obligations comparable in scope to those herein, which are in no event less
                    than a reasonable standard of care.
                  </p>
                </li>
                <li>
                  <h3>5.3 Protection</h3>
                  <p>
                    You shall use at least the same level of care to prevent unauthorized use of the Confidential Information as it uses for
                    its own confidential and proprietary information of like kind, but in no event less than a reasonable standard of care.
                    The confidentiality obligations contained herein supersede and replace any prior non-disclosure regarding the subject
                    matter covered by these Terms.
                  </p>
                </li>
                <li>
                  <h3>5.4 Compelled Disclosure</h3>
                  <p>
                    We may disclose Confidential Information if so required pursuant to a regulation, law, subpoena, or court order
                    (“Compelled Disclosures”), provided that we give you a notice of a Compelled Disclosure to the extent legally permitted.
                    You shall provide reasonable cooperation to us in connection with a Compelled Disclosure.
                  </p>
                </li>
              </ol>
            </div>
            <div class="section" id="ts-s6">
              <h2>6. Third-Party Contents</h2>
              <p>
                The Website or Services may contain links to third-party websites, advertisers, services, special offers, or other events or
                activities that are not owned or controlled by us (“Third-Party Contents”). We do not endorse or assume any responsibility
                for any Third-Party Contents. If you access any Third-Party Contents through or in connection with your access or use of our
                Website or Services, you do so at your own risk and you agree that we will have no liability arising from your use of or
                access to any Third-Party Contents.
              </p>
            </div>
            <div class="section" id="ts-s7">
              <h2>7. Disclaimer</h2>
              <p>
                YOU EXPRESSLY UNDERSTAND AND AGREE THAT TO THE EXTENT PERMITTED BY APPLICABLE LAW, YOUR USE OF THE WEBSITE AND SERVICES IS
                AT YOUR SOLE RISK, AND THE WEBSITE AND SERVICES ARE PROVIDED ON AN “AS IS” AND “AS AVAILABLE” BASIS, WITH ALL FAULTS. WE
                EXPRESSLY DISCLAIM ALL WARRANTIES, REPRESENTATIONS, AND CONDITIONS OF ANY KIND, WHETHER EXPRESS OR IMPLIED, INCLUDING, BUT
                NOT LIMITED TO, THE IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
                NON-INFRINGEMENT.
              </p>
              <p>WE MAKE NO WARRANTY, REPRESENTATION OR CONDITION THAT:</p>
              <ol class="list-style-decimal">
                <li>THE WEBSITE OR SERVICES WILL MEET YOUR REQUIREMENTS;</li>
                <li>YOUR USE OF THE WEBSITE OR SERVICES WILL BE UNINTERRUPTED, TIMELY, SECURE OR ERROR-FREE;</li>
                <li>THE RESULTS THAT MAY BE OBTAINED FROM USE OF THE WEBSITE OR SERVICES WILL BE ACCURATE OR RELIABLE; OR</li>
                <li>
                  ANY ERRORS IN THE WEBSITE OR SERVICES WILL BE CORRECTED. THE SERVICES MAY BE SUBJECT TO DELAYS, CANCELLATIONS AND OTHER
                  DISRUPTIONS.
                </li>
              </ol>
              <p>
                NO ADVICE OR INFORMATION, WHETHER ORAL OR WRITTEN, OBTAINED FROM US OR THROUGH THE WEBSITE OR SERVICE WILL CREATE ANY
                WARRANTY NOT EXPRESSLY MADE HEREIN.
              </p>
            </div>
            <div class="section" id="ts-s8">
              <h2>8. Indemnification</h2>
              <p>
                You agree to indemnify, defend and hold harmless us, our corporate parents, subsidiaries, and affiliates, and the officers,
                directors, employees, agents, representatives, partners and licensors of each from any damages, losses, costs, liabilities
                and expenses (including reasonable attorneys’ fees) relating to or arising out of any claims concerning:
              </p>
              <ol class="list-style-lowerAlpha">
                <li>your use, or inability to use the Website or Services;</li>
                <li>your misuse of the Website or Services or violation of any of these Terms;</li>
                <li>your violation of any rights of another party; or</li>
                <li>your violation of any applicable laws, rules or regulations.</li>
              </ol>
              <p>
                We reserve the right, at our own cost, to assume the exclusive defense and control of any matter otherwise subject to
                indemnification by you, in which event you will fully cooperate with us in asserting any available defenses. You agree that
                the provisions in this section will survive any termination of your account, these Terms or your access to the Website or
                Services.
              </p>
            </div>
            <div class="section" id="ts-s9">
              <h2>9. Limitation of Liability</h2>
              <ol>
                <li>
                  <h3>9.1 Disclaimer of Certain Damages</h3>
                  <p>
                    TO THE FULLEST EXTENT PERMITTED BY APPLICABLE LAW, WE SHALL NOT BE LIABLE FOR ANY LOSS OF PROFITS OR REVENUE, LOSS OF
                    GOODWILL, ACCURACY OF RESULTS OR FOR INDIRECT, INCIDENTAL, PUNITIVE, EXEMPLARY, SPECIAL OR CONSEQUENTIAL DAMAGES, OR
                    DAMAGES OR COSTS DUE TO: THE WEBSITE OR SERVICES, INCLUDING YOUR USE OR INABILITY TO USE THEREOF; ANY CONTENT PROVIDED
                    BY US; ANY ACTION TAKEN IN CONNECTION WITH ANY THIRD-PARTY INTELLECTUAL PROPERTY CLAIMS; LOSS OF DATA; PRODUCTION; OR,
                    BUSINESS INTERRUPTION OR PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES, REGARDLESS OF WHETHER WE HAVE BEEN ADVISED OF THE
                    POSSIBILITY OF SUCH DAMAGES.
                  </p>
                </li>
                <li>
                  <h3>9.2 Limitation of Liability</h3>
                  <p>
                    UNDER NO CIRCUMSTANCES WILL WE BE LIABLE TO YOU FOR MORE THAN THE AMOUNT RECEIVED BY US AS A RESULT OF YOUR USE OF OUR
                    WEBSITE AND SERVICES IN THE SUBSCRIPTION PERIOD DURING WHICH YOU FIRST ASSERT A CLAIM. IF YOU HAVE NOT PAID THE COMPANY
                    ANY AMOUNTS IN THE SUBSCRIPTION PERIOD DURING WHICH YOU FIRST ASSERT ANY SUCH CLAIM, OUR SOLE AND EXCLUSIVE LIABILITY
                    SHALL BE LIMITED TO FIFTY DOLLARS ($50).
                  </p>
                </li>
                <li>
                  <h3>9.3 Exceptions</h3>
                  <p>
                    SOME JURISDICTIONS DO NOT ALLOW FOR THE EXCLUSION OR LIMITATION OF CERTAIN DAMAGES. IF THESE LAWS APPLY TO YOU, SOME OR
                    ALL OF THE DISCLAIMERS, EXCLUSIONS, AND LIMITATIONS MAY NOT APPLY TO YOU AND YOU MIGHT HAVE ADDITIONAL RIGHTS.
                  </p>
                </li>
              </ol>
            </div>
            <div class="section" id="ts-s10">
              <h2>10. Termination</h2>
              <p>
                At our sole discretion, we may modify or discontinue the Website or Services, or may modify, suspend or terminate your
                access to the Website or Services, for any reason, with or without notice to you and without liability to you or any third
                party. In addition to suspending or terminating your access to the Website or Services, we reserve the right to take
                appropriate legal action, including without limitation, pursuing civil, criminal or injunctive redress. Even after your
                right to use the Website or Services is terminated, these Terms will remain enforceable against you and unpaid amounts you
                owe us will remain due. All sections which by their nature should survive the termination of these Terms shall continue in
                full force and effect notwithstanding any termination of these Terms.
              </p>
            </div>
            <div class="section" id="ts-s11">
              <h2>11. General</h2>
              <ol>
                <li>
                  <h3>11.1 Assignment</h3>
                  <p>
                    These terms and your rights and obligations hereunder may not be assigned, subcontracted, delegated or otherwise
                    transferred by you without our prior written consent, and any attempted assignment, subcontract, delegation, or transfer
                    in violation of the foregoing will be null and void.
                  </p>
                </li>
                <li>
                  <h3>11.2 Electric Communications</h3>
                  <p>
                    We will communicate with you through electronic means, whether we post notices on our Website or Services, communicate
                    with you via e-mail, or you visit the Website or Services. For contractual purposes, you
                  </p>
                  <ol class="list-style-lowerAlpha">
                    <li>onsent to receive communications from us in an electronic form; and</li>
                    <li>
                      agree that all terms and conditions, agreements, notices, disclosures, and other communications and documents that we
                      provide you electronically will have the same legal effect that such communications or documents would have if they
                      were set forth in paper-based “writing.”
                    </li>
                  </ol>
                </li>
                <li>
                  <h3>11.3 Notices</h3>
                  <p>
                    You agree that we may provide you notices by email, regular mail, or postings on the Website or Services. You may
                    provide notice to us by contacting us at <a href="mailto:support@deepsignal.ai">support@deepsignal.ai</a>.
                  </p>
                </li>
                <li>
                  <h3>11.4 Third-Party Data</h3>
                  <p>
                    At our sole discretion, we may modify or discontinue the Website or Services, or may modify, suspend or terminate your
                    access to the Website or Services, for any reason, with or without notice to you and without liability to you or any
                    third party. In addition to suspending or terminating your access to the Website or Services, we reserve the right to
                    take appropriate legal action, including without limitation, pursuing civil, criminal or injunctive redress. Even after
                    your right to use the Website or Services is terminated, these Terms will remain enforceable against you and unpaid
                    amounts you owe us will remain due. All sections which by their nature should survive the termination of these Terms
                    shall continue in full force and effect notwithstanding any termination of these Terms.
                  </p>
                </li>
                <li>
                  <h3>11.5 Relationship of Parties</h3>
                  <p>
                    The parties are independent contractors. These terms do not create a partnership, franchise, joint venture, agency,
                    fiduciary or employment relationship between the parties.
                  </p>
                </li>
                <li>
                  <h3>11.6 Severability</h3>
                  <p>
                    If any portion of these Terms is held invalid or unenforceable, that portion shall be construed in a manner to reflect,
                    as nearly as possible, the original intention of the parties, and the remaining portions shall remain in full force and
                    effect.
                  </p>
                </li>
                <li>
                  <h3>11.7 Force Majeure</h3>
                  <p>
                    We shall not be liable for any delay or failure to perform resulting from causes outside our reasonable control,
                    including, but not limited to, acts of God, war, epidemics, pandemics, terrorism, riots, embargos, acts of civil or
                    military authorities, fire, floods, accidents, strikes or shortages of transportation facilities, fuel, energy, labor or
                    materials.
                  </p>
                </li>
                <li>
                  <h3>11.8 Governing Law and Venue</h3>
                  <p>
                    These terms and any disputes arising out of these terms shall be governed by and construed in accordance with the laws
                    of the State of California, without giving effect to its conflicts of laws rules, the United Nations Convention on the
                    International Sale of Goods, or the Uniform Computer Information Transactions Act.
                  </p>
                </li>
                <li>
                  <h3>11.9 Venue; Waiver of Jury Trial</h3>
                  <p>
                    The state and Federal courts located in Santa Clara County, California shall have exclusive jurisdiction to adjudicate
                    any dispute arising out of or relating to these Terms. Each party hereby consents to the exclusive jurisdiction of such
                    courts and waives any objection it might otherwise have to venue, personal jurisdiction, inconvenience of forum, and any
                    similar or related doctrine. Each party also hereby waives any right to jury trial in connection with any action or
                    litigation in any way arising out these Terms.
                  </p>
                </li>
                <li id="ts-s11-10">
                  <h3>11.10 Dispute Resolution</h3>
                  <p>
                    In the interest of resolving disputes between DeepSignal and you in the most expedient and cost-effective manner, both
                    parties agree that every dispute arising in connection with these Terms will be resolved by binding arbitration.
                    Arbitration is less formal than a lawsuit in court. Arbitration uses a neutral arbitrator instead of a judge or jury,
                    may allow for more limited discovery than in court, and can be subject to very limited review by courts. Arbitrators can
                    award the same damages and relief that a court can award. The agreement to arbitrate disputes includes all claims
                    arising out of or relating to any aspect of these Terms, whether based in contract, tort, statute, fraud,
                    misrepresentation, or any other legal theory, and regardless of whether a claim arises during or after the termination
                    of these Terms. YOU UNDERSTAND AND AGREE THAT, BY ENTERING INTO THESE TERMS, BOTH PARTIES ARE WAIVING THE RIGHT TO A
                    TRIAL BY JURY OR TO PARTICIPATE IN A CLASS ACTION.
                  </p>
                </li>
                <li>
                  <h3>11.11 Waiver</h3>
                  <p>
                    Any waiver or failure to enforce any provision of these Terms on one occasion will not be deemed a waiver of any other
                    provision or of such provision on any other occasion.
                  </p>
                </li>
                <li>
                  <h3>11.12 Notice to California Residents</h3>
                  <p>
                    Under California Civil Code Section 1789.3, California residents are entitled to the following specific consumer rights
                    information:
                  </p>
                  <p>The provider of the Website is:</p>
                  <ul class="list-style-disc">
                    <li>DeepSignal LLC.</li>
                    <li>440 N Wolfe Rd.</li>
                    <li>Sunnyvale, CA 94085</li>
                    <!-- <li>이메일 : <a href="mailto:support@deepsignal.ai">support@deepsignal.ai</a></li> -->
                  </ul>
                  <p>
                    To file a complaint or to receive further information regarding use of the Website or Services, send a letter to the
                    above address or contact us via e-mail or telephone.
                  </p>
                </li>
                <li>
                  <h3>11.13 International Access</h3>
                  <p>
                    The Website and Services may be accessed from countries other than the United States. We make no representations or
                    warranties that the Website or Services are appropriate for use or access in other locations. If you access and use the
                    Site or the Service outside the United States, you are responsible for complying with both United States and applicable
                    local laws and regulations.
                  </p>
                </li>
                <li>
                  <h3>11.14 Export Control</h3>
                  <p>
                    The Website, Services, and other technology we make available, and derivatives thereof may be subject to export laws and
                    regulations of the United States and other jurisdictions. In particular, the Website and Services shall not be exported
                    or re-exported
                  </p>
                  <ol class="list-style-lowerAlpha">
                    <li>into any United States embargoed countries, or</li>
                    <li>
                      to anyone on the U.S. Treasury Department’s list of Specially Designated Nationals or the U.S. Department of
                      Commerce’s Denied Person’s List or Entity List.
                    </li>
                  </ol>
                  <P>By using the Website or Services, you represent and warrant that</P>
                  <ol class="list-style-lowerAlpha">
                    <li>
                      you are not located in a country that is subject to a U.S. Government embargo, or that has been designated by the U.S.
                      Government as a “terrorist supporting” country and
                    </li>
                    <li>you are not listed on any U.S. Government list of prohibited or restricted parties.</li>
                  </ol>
                  <p>
                    You also will not use the Website or Services for any purpose prohibited by U.S. law, including the development, design,
                    manufacture or production of missiles, nuclear, chemical or biological weapons.
                  </p>
                </li>
                <li>
                  <h3>11.15 Modifications to these Terms</h3>
                  <p>
                    We reserve the right to modify these Terms including any referenced policies and other documents, effective upon the
                    commencement of any renewal term. If you object to the updated Terms, as your exclusive remedy, you may choose not to
                    renew your subscription to our services. If you do not agree to the updated Terms after it becomes effective, you will
                    no longer have a right to use the Website or Services.
                  </p>
                </li>
                <li>
                  <h3>11.16 Entire Agreement</h3>
                  <p>
                    These Terms are the final, complete and exclusive agreement of the parties with respect to the subject matter hereof and
                    supersedes and merges all prior discussions between the parties with respect to such subject matter.
                  </p>
                </li>
              </ol>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button
          type="button"
          class="btn btn-default"
          data-dismiss="modal"
          v-text="$t('register.terms-of-use.confirm')"
          @click="$bvModal.hide('termOfUse')"
        >
          확인
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./terms-of-use.component.ts"></script>
