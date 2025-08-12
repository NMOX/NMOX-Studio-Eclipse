package com.nmox.studio.editor.html;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.rules.ITokenScanner;

public class HTMLConfiguration extends SourceViewerConfiguration {

    private HTMLScanner htmlScanner;
    private HTMLTagScanner tagScanner;

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getHTMLScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        DefaultDamagerRepairer tagDR = new DefaultDamagerRepairer(getHTMLTagScanner());
        reconciler.setDamager(tagDR, HTMLPartitionScanner.HTML_TAG);
        reconciler.setRepairer(tagDR, HTMLPartitionScanner.HTML_TAG);

        return reconciler;
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant = new ContentAssistant();
        assistant.setContentAssistProcessor(new HTMLContentAssistProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
        assistant.enableAutoActivation(true);
        assistant.setAutoActivationDelay(500);
        assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
        return assistant;
    }

    protected ITokenScanner getHTMLScanner() {
        if (htmlScanner == null) {
            htmlScanner = new HTMLScanner();
        }
        return htmlScanner;
    }

    protected ITokenScanner getHTMLTagScanner() {
        if (tagScanner == null) {
            tagScanner = new HTMLTagScanner();
        }
        return tagScanner;
    }
}